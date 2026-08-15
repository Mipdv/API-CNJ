// Caminho relativo: funciona quando o HTML é servido pelo próprio Spring Boot
// (arquivo dentro de src/main/resources/static/). Se abrir o HTML solto no
// navegador (fora do backend), troque para 'http://localhost:8080/processo'.
const API_BASE = '/processo';
const MOVIMENTOS_POR_PAGINA = 8;

let ultimoResultado = null;
let paginaAtual = 1;

const $numero = document.getElementById('numero');
const $btnConsultar = document.getElementById('btnConsultar');
const $loading = document.getElementById('loading');
const $erro = document.getElementById('erro');
const $resultado = document.getElementById('resultado');
const $capaVisivel = document.getElementById('capaVisivel');
const $btnPdf = document.getElementById('btnPdf');

// Máscara automática do número CNJ
$numero.addEventListener('input', () => {
  let v = $numero.value.replace(/\D/g, '').slice(0, 20);
  let out = v;
  if (v.length > 7)  out = v.slice(0,7) + '-' + v.slice(7);
  if (v.length > 9)  out = out.slice(0,10) + '.' + out.slice(10);
  if (v.length > 13) out = out.slice(0,15) + '.' + out.slice(15);
  if (v.length > 14) out = out.slice(0,17) + '.' + out.slice(17);
  if (v.length > 16) out = out.slice(0,20) + '.' + out.slice(20);
  $numero.value = out;
});

$numero.addEventListener('keydown', e => { if (e.key === 'Enter') consultar(); });
$btnConsultar.addEventListener('click', consultar);
$btnPdf.addEventListener('click', gerarPdf);

async function consultar() {
  const numeroLimpo = $numero.value.replace(/\D/g, '');

  if (numeroLimpo.length !== 20) {
    mostrarErro('Informe os 20 dígitos do número do processo.');
    return;
  }

  $resultado.style.display = 'none';
  $erro.style.display = 'none';
  $loading.style.display = 'block';
  $btnConsultar.disabled = true;

  try {
    const resp = await fetch(`${API_BASE}/${numeroLimpo}`, { method: 'POST' });

    if (!resp.ok) {
      const texto = await resp.text().catch(() => '');
      throw new Error(texto || `Erro ao consultar (HTTP ${resp.status})`);
    }

    const dados = await resp.json();
    ultimoResultado = dados;
    paginaAtual = 1;
    renderizar(dados);
    $resultado.style.display = 'block';

  } catch (e) {
    mostrarErro(e.message || 'Não foi possível consultar o processo. Verifique o número ou tente novamente.');
  } finally {
    $loading.style.display = 'none';
    $btnConsultar.disabled = false;
  }
}

function mostrarErro(msg) {
  $erro.textContent = msg;
  $erro.style.display = 'block';
}

function formatarNumero(numero) {
  if (!numero || numero.length !== 20) return numero || '—';
  return `${numero.slice(0,7)}-${numero.slice(7,9)}.${numero.slice(9,13)}.${numero.slice(13,14)}.${numero.slice(14,16)}.${numero.slice(16,20)}`;
}

// A API já devolve as datas formatadas (dd/MM/yyyy) — o front só exibe.
function formatarData(valor) {
  return valor && valor.trim() ? valor : '—';
}

function formatarSistema(valor) {
  return valor && valor.trim() ? valor : 'Não foi possível determinar';
}
// Nome de exibição de cada movimentação.
// O backend expõe o campo como "nomeDoAto" (MovimentoDTOResponse) —
// se você renomear esse campo no Java, ajuste aqui também.
function nomeMovimento(m) {
  return m.nomeDoAto || m.nome || '';
}

function montarCapaHTML(dados, movimentosParaExibir, mostrarPaginacao) {
  const assuntos = dados.assuntos || [];
  const movimentos = dados.movimentos || [];

  const assuntosHTML = assuntos.length
    ? `<div class="assuntos">${assuntos.map(a => `<span class="assunto-chip">${escapeHtml(a.nome)}</span>`).join('')}</div>`
    : `<p class="empty-note">Nenhum assunto informado.</p>`;

  const movimentosHTML = movimentosParaExibir.length
    ? `<ul class="movimentos-list">${movimentosParaExibir.map(m => `
        <li class="movimento-item">
          <span class="data">${formatarData(m.dataHora)}</span>
          <span class="nome">${escapeHtml(nomeMovimento(m))}</span>
        </li>`).join('')}</ul>`
    : `<p class="empty-note">Nenhuma movimentação disponível.</p>`;

  const paginacaoHTML = mostrarPaginacao ? montarPaginacaoHTML(movimentos.length) : '';

  return `
    <p class="numero">${formatarNumero(dados.numeroProcesso)}</p>
    <h1 class="classe">${escapeHtml(dados.tipoDaAcao || 'Classe não informada')}</h1>

    <div class="meta-grid">
          <div class="meta-item">
            <div class="label">Tribunal</div>
            <div class="value">${escapeHtml((dados.tribunal || '—').toUpperCase())} <span style="color:var(--slate);">/ ${escapeHtml(dados.estado || '—')}</span></div>
          </div>
          <div class="meta-item">
            <div class="label">Grau</div>
            <div class="value">${escapeHtml(dados.grau || '—')}</div>
          </div>
          <div class="meta-item">
            <div class="label">Sistema</div>
            <div class="value">${escapeHtml(formatarSistema(dados.sistema))}</div>
          </div>
          <div class="meta-item">
            <div class="label">Data de ajuizamento</div>
            <div class="value">${formatarData(dados.dataAjuizamento)}</div>
          </div>
          <div class="meta-item">
            <div class="label">Última atualização</div>
            <div class="value">${formatarData(dados.ultimaAtualizacao)}</div>
          </div>
        </div>

    <p class="section-title">Assuntos</p>
    ${assuntosHTML}

    <p class="section-title">Movimentações (fls. ${movimentos.length ? '1–' + movimentos.length : '—'})</p>
    ${movimentosHTML}
    ${paginacaoHTML}
  `;
}

function montarPaginacaoHTML(totalMovimentos) {
  const totalPaginas = Math.max(1, Math.ceil(totalMovimentos / MOVIMENTOS_POR_PAGINA));
  if (totalPaginas <= 1) return '';

  let botoes = `<span class="fls-label">fls.</span>`;
  botoes += `<button class="fls-btn" id="flsAnterior" ${paginaAtual === 1 ? 'disabled' : ''}>‹</button>`;
  for (let i = 1; i <= totalPaginas; i++) {
    botoes += `<button class="fls-btn ${i === paginaAtual ? 'active' : ''}" data-pagina="${i}">${String(i).padStart(2,'0')}</button>`;
  }
  botoes += `<button class="fls-btn" id="flsProxima" ${paginaAtual === totalPaginas ? 'disabled' : ''}>›</button>`;

  return `<div class="paginacao">${botoes}</div>`;
}

function renderizar(dados) {
  const movimentos = dados.movimentos || [];
  const inicio = (paginaAtual - 1) * MOVIMENTOS_POR_PAGINA;
  const pagina = movimentos.slice(inicio, inicio + MOVIMENTOS_POR_PAGINA);

  $capaVisivel.innerHTML = montarCapaHTML(dados, pagina, true);

  const totalPaginas = Math.max(1, Math.ceil(movimentos.length / MOVIMENTOS_POR_PAGINA));
  document.querySelectorAll('.fls-btn[data-pagina]').forEach(btn => {
    btn.addEventListener('click', () => { paginaAtual = parseInt(btn.dataset.pagina, 10); renderizar(dados); });
  });
  const anterior = document.getElementById('flsAnterior');
  const proxima = document.getElementById('flsProxima');
  if (anterior) anterior.addEventListener('click', () => { if (paginaAtual > 1) { paginaAtual--; renderizar(dados); } });
  if (proxima)  proxima.addEventListener('click', () => { if (paginaAtual < totalPaginas) { paginaAtual++; renderizar(dados); } });
}

function escapeHtml(str) {
  const div = document.createElement('div');
  div.textContent = str;
  return div.innerHTML;
}

function gerarPdf() {
  if (!ultimoResultado) return;

  // Pro PDF, monta a capa com TODOS os movimentos (sem paginação/sem botões)
  const pdfSource = document.getElementById('pdf-source');
  pdfSource.innerHTML = `<div class="capa">${montarCapaHTML(ultimoResultado, ultimoResultado.movimentos || [], false)}</div>`;

  const nomeArquivo = `processo_${(ultimoResultado.numeroProcesso || 'consulta')}.pdf`;

  html2pdf()
    .set({
      margin: 12,
      filename: nomeArquivo,
      html2canvas: { scale: 2 },
      jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
    })
    .from(pdfSource)
    .save();
}
