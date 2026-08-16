# API-Consulta-CNJ

API REST em Spring Boot para consulta processual, usando a base pública do **Datajud (CNJ)** como fonte de dados, com **cache local em Postgres**

---

## Por que existe cache

A API pública do Datajud tem **limite de requisições** e não é instantânea — cada chamada nova percorre o Elasticsearch do CNJ, atravessa a rede até o tribunal de origem, e volta. Se dois usuários consultarem o mesmo processo no mesmo dia, não faz sentido pagar esse custo duas vezes: os dados de um processo não mudam a cada segundo.

Por isso, toda consulta passa primeiro pelo banco local antes de acionar o Datajud:

```
Consulta chega
      │
      ▼
Existe no banco? ──não──► Consulta o Datajud ──► Salva no banco ──► Devolve
      │
     sim
      │
      ▼
Cache tem menos de 24h? ──sim──► Devolve direto do banco (sem chamar o Datajud)
      │
     não (expirado)
      │
      ▼
Consulta o Datajud de novo ──► Atualiza o registro ──► Devolve
```

Essa lógica vive em `ProcessoService.consultarProcesso(...)`:

```java
Optional<Processo> cache = processoRepository.findByNumeroProcesso(numero);

if (cache.isPresent()) {
    Processo processo = cache.get();
    if (processo.getDataConsulta() != null &&
            processo.getDataConsulta().isAfter(LocalDateTime.now().minusHours(24))) {
        return converterParaResponse(processo); // cache válido — nem toca no Datajud
    }
    // cache expirado — consulta de novo e atualiza
}
```

**O que isso garante na prática:**
- **Menos chamadas ao Datajud** — evita estourar limite de requisições da API pública em picos de uso.
- **Resposta muito mais rápida** em consultas repetidas — vem do Postgres local em vez de ida e volta pela internet até o CNJ.
- **Dados sempre "frescos" o suficiente** — 24h é uma janela curta pra esse tipo de dado (movimentação processual não muda de hora em hora), então não há risco real de mostrar informação desatualizada de forma relevante.
- **Histórico persistido** — mesmo que o Datajud tenha instabilidade momentânea, processos já consultados uma vez continuam disponíveis.

O cache também é **inteligente sobre o que reaproveita**: assuntos (`Assunto`) são compartilhados entre processos diferentes (`@ManyToMany`) — se dois processos tratam do mesmo assunto, não duplica a linha no banco, só reaproveita a existente.

---

## Arquitetura resumida

```
Cliente (navegador)
      │  POST /processo/{numero}
      ▼
ProcessoController
      │
      ▼
ProcessoService ──► TribunalResolver (extrai tribunal/UF do próprio número do processo)
      │
      ├──► Cache válido? ──► devolve do Postgres
      │
      └──► Cache ausente/expirado
                │
                ▼
           CnjClient (Feign) ──► API pública Datajud (Elasticsearch)
                │
                ▼
           Salva/atualiza no Postgres (Processo, Movimento, Assunto)
                │
                ▼
           ProcessoDTOResponse (JSON limpo, datas em dd/MM/yyyy, pt-BR)
```

**Não é preciso informar o tribunal na consulta** — o `TribunalResolver` decodifica os segmentos `J` (justiça) e `TR` (tribunal) já embutidos no próprio número CNJ (formato `NNNNNNN-DD.AAAA.J.TR.OOOO`) e resolve automaticamente o alias do índice e a UF.

---

## Como rodar — só com Docker Desktop

Não precisa ter Java, Maven nem Postgres instalados na máquina. O `Dockerfile` é multi-stage: o próprio container compila o projeto (baixa JDK e Maven descartáveis) e só o `.jar` final vai pra imagem que roda.

### 1. Pré-requisito

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e aberto.

### 2. Clonar o projeto

```bash
git clone <url-do-repositorio>
cd api-consulta-cnj
```

### 3. Criar o arquivo `.env`

Na raiz do projeto, ao lado do `docker-compose.yml`:

```env
DB_USERNAME=postgres
DB_PASSWORD=uma_senha_forte_aqui
CNJ_API_TOKEN=cole_aqui_a_chave_publica_do_datajud
```

A chave pública do Datajud está disponível na documentação oficial do CNJ: https://datajud-wiki.cnj.jus.br/api-publica/

### 4. Subir tudo

```bash
docker compose up --build
```

`docker compose` orquestra os dois serviços do projeto (`app` + `postgres`) a partir do `docker-compose.yml` — não é o mesmo que `docker build`, que só constrói uma imagem isolada sem subir o banco nem ler o `.env`. O `--build` força reconstruir a imagem a partir do `Dockerfile` em vez de reaproveitar uma imagem antiga (importante sempre que o código mudou).

Na primeira vez demora um pouco mais (baixa as imagens base); nas próximas, o Docker reaproveita cache de camadas.

Aguarda aparecer no log: `Started ApiConsultaCnjApplication`.

### 5. Acessar

```
http://localhost:8080
```

### Parar

```bash
docker compose down
```

Pra também apagar os dados do banco (recomeçar do zero, sem cache acumulado):
```bash
docker compose down -v
```

---

## Endpoint

```
POST /processo/{numero}
```

- `{numero}` — número CNJ do processo, 20 dígitos, com ou sem máscara (a API remove pontuação automaticamente).
- Sem tribunal na URL — é resolvido a partir do próprio número.
- Sem corpo de requisição.

**Exemplo:**
```bash
curl -X POST http://localhost:8080/processo/00009794120185130002
```

**Resposta:**
```json
{
  "numeroProcesso": "00009794120185130002",
  "dataAjuizamento": "20/02/2020",
  "tribunal": "TRT13",
  "grau": "G2",
  "sistema": "PJe",
  "estado": "PB",
  "tipoDaAcao": "Recurso Ordinário Trabalhista",
  "ultimaAtualizacao": "07/05/2024",
  "assuntos": [
    { "nome": "Verbas Rescisórias" }
  ],
  "movimentos": [
    { "nomeDoAto": "Distribuição", "dataHora": "20/02/2020" }
  ]
}
```

Todas as datas já saem formatadas em português brasileiro (`dd/MM/yyyy`), convertidas do fuso de origem (o Datajud manda em UTC) — o cliente não precisa fazer nenhum parse.

---

## Estrutura do projeto

```
src/main/java/com/Mipdv/api_consulta_cnj/
├── business/
│   ├── controller/       → ProcessoController (endpoint HTTP), ExceptionHandler
│   ├── dtoResponse/       → DTOs de saída da própria API (ProcessoDTOResponse, AssuntoDTOResponse, MovimentoDTOResponse)
│   └── service/           → ProcessoService (orquestra cache + consulta + conversão)
├── infrastructure/
│   ├── Client/             → CnjClient (Feign), FeignConfig, FeignError (tratamento de erro HTTP)
│   ├── dtoRequest/        → DTOs da query Elasticsearch enviada ao Datajud
│   ├── dtoResponse/        → DTOs que espelham o JSON bruto do Datajud (DataJudResponseDTO → HitsWrapperDTO → HitDTO → ProcessoCnjDTO)
│   ├── entity/             → Processo, Movimento, Assunto (JPA)
│   ├── exceptions/        → BadRequestException, ConflictException, UnauthorizedException
│   ├── repository/        → processoRepository, movimentoRepository, assuntoRepository
│   └── util/               → TribunalResolver (número CNJ → tribunal/UF), DataUtil (formatação de datas)
└── ApiConsultaCnjApplication.java

src/main/resources/
└──  application.properties
```

## Funcionalidades

- ✅ Consulta processual por número CNJ (20 dígitos), sem precisar informar o tribunal manualmente, além de realizar consultas com máscaras processuais;
- ✅ Cache local de 24h — reduz chamadas ao Datajud e acelera consultas repetidas;
- ✅ Resolução automática de tribunal/UF a partir do número do processo;
- ✅ Datas formatadas em português (`dd/MM/yyyy`), com conversão correta de fuso horário;
- ✅ Tratamento de erros HTTP do Datajud (400/401/403/409) mapeado para exceções próprias;

## Problemas comuns

| Sintoma | Causa provável |
|---|---|
| Erro de conexão com o banco | `.env` não foi criado, ou `DB_PASSWORD` está vazio |
| 401/403 nas consultas | `CNJ_API_TOKEN` errado ou não configurado no `.env` |
| `Tribunal não mapeado` | Número de processo válido, mas de um tribunal fora da tabela do `TribunalResolver` |
| Dado desatualizado numa consulta repetida | Esperado dentro da janela de 24h de cache — force nova consulta apagando o registro do banco se precisar de dado em tempo real |
