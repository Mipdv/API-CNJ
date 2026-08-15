package com.Mipdv.api_consulta_cnj.business.service;

import com.Mipdv.api_consulta_cnj.business.dtoResponse.*;
import com.Mipdv.api_consulta_cnj.infrastructure.Client.CnjClient;
import com.Mipdv.api_consulta_cnj.infrastructure.dtoRequest.ProcessoDTORequest;
import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.*;
import com.Mipdv.api_consulta_cnj.infrastructure.entity.Assunto;
import com.Mipdv.api_consulta_cnj.infrastructure.entity.Movimento;
import com.Mipdv.api_consulta_cnj.infrastructure.entity.Processo;
import com.Mipdv.api_consulta_cnj.infrastructure.exceptions.ConflictException;
import com.Mipdv.api_consulta_cnj.infrastructure.repository.assuntoRepository;
import com.Mipdv.api_consulta_cnj.infrastructure.repository.processoRepository;
import com.Mipdv.api_consulta_cnj.infrastructure.util.DataUtil;
import com.Mipdv.api_consulta_cnj.infrastructure.util.TribunalInfo;
import com.Mipdv.api_consulta_cnj.infrastructure.util.TribunalResolver;
import lombok.RequiredArgsConstructor;
import com.Mipdv.api_consulta_cnj.infrastructure.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessoService {

    private final processoRepository processoRepository;
    private final assuntoRepository assuntoRepository;
    private final CnjClient cnjClient;
    private final TribunalResolver tribunalResolver;



    public ProcessoDTOResponse consultarProcesso(String numero) {
        if (numero == null || numero.length() != 20) {
            try {
                throw new BadRequestException("Número do processo inválido.");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }

        }
        TribunalInfo tribunalInfo = tribunalResolver.resolver(numero);
        String tribunal = tribunalInfo.alias();
        String estado = tribunalInfo.estado();

        Optional<Processo> cache = processoRepository.findByNumeroProcesso(numero);

        if (cache.isPresent()) {
            Processo processo = cache.get();
            if (processo.getDataConsulta() != null &&
                    processo.getDataConsulta().isAfter(LocalDateTime.now().minusHours(24))) {
                return converterParaResponse(processo);
            }
            ProcessoCnjDTO dto = consultarApiCnj(tribunal, numero);
            atualizarEntity(processo, dto ,estado);
            return converterParaResponse(salvarConsulta(processo));
        }

        ProcessoCnjDTO dto = consultarApiCnj(tribunal, numero);
        Processo processo = converterParaEntity(dto , estado);
        return converterParaResponse(salvarConsulta(processo));
    }

    public Processo salvarConsulta(Processo processo) {
        processo.setDataConsulta(LocalDateTime.now());
        return processoRepository.save(processo);
    }

    private ProcessoCnjDTO consultarApiCnj(String tribunal, String numero) {
        try {
            ProcessoDTORequest request = ProcessoDTORequest.porNumeroProcesso(numero);
            DataJudResponseDTO resposta = cnjClient.consultar(tribunal, request);

            if (resposta.getHits() == null || resposta.getHits().getHits().isEmpty()) {
                throw new ConflictException("Processo não encontrado no DataJud");
            }
            return resposta.getHits().getHits().get(0).getSource();

        } catch (ConflictException e) {
            throw e;
        } catch (Exception e) {
            throw new ConflictException("Erro ao consultar o processo", e);
        }
    }

    private Processo converterParaEntity(ProcessoCnjDTO dto, String estado) {
        Processo processo = new Processo();
        preencherCamposComuns(processo, dto, estado);
        return processo;
    }

    private void atualizarEntity(Processo processo, ProcessoCnjDTO dto, String estado) {
        preencherCamposComuns(processo, dto , estado);
    }

    private void preencherCamposComuns(Processo processo, ProcessoCnjDTO dto , String estado) {
        processo.setNumeroProcesso(dto.getNumeroProcesso());
        processo.setTribunal(dto.getTribunal());
        processo.setEstado(estado);
        processo.setGrau(dto.getGrau());
        processo.setDataAjuizamento(dto.getDataAjuizamento());
        processo.setUltimaAtualizacao(dto.getDataHoraUltimaAtualizacao());

        if (dto.getClasse() != null) {
            processo.setCodigo(dto.getClasse().getCodigo());
            processo.setClasseNome(dto.getClasse().getNome());
        }

        if(dto.getSistema() != null){
            processo.setSistemaNome(dto.getSistema().getNome());
        }

        atualizarAssuntos(processo, dto.getAssuntos());
        atualizarMovimentos(processo, dto.getMovimentos());
    }

    private void atualizarMovimentos(Processo processo, List<MovimentoCnjDTO> movimentosDto) {
        if (processo.getMovimentos() == null) {
            processo.setMovimentos(new ArrayList<>());
        }
        processo.getMovimentos().clear();

        if (movimentosDto != null) {
            for (MovimentoCnjDTO m : movimentosDto) {
                Movimento mov = new Movimento();
                mov.setCodigo(m.getCodigo());
                mov.setNome(m.getNome());
                mov.setDataHora(m.getDataHora());
                mov.setProcesso(processo);
                processo.getMovimentos().add(mov);
            }
        }
    }

    private void atualizarAssuntos(Processo processo, List<AssuntoCnjDTO> assuntosDto) {
        if (processo.getAssuntos() == null) {
            processo.setAssuntos(new ArrayList<>());
        }
        processo.getAssuntos().clear();

        if (assuntosDto != null) {
            for (AssuntoCnjDTO a : assuntosDto) {
                Assunto assunto = assuntoRepository.findByNome(a.getNome())
                        .orElseGet(() -> {
                            Assunto novo = new Assunto();
                            novo.setNome(a.getNome());
                            return assuntoRepository.save(novo);
                        });
                processo.getAssuntos().add(assunto);
            }
        }
    }

    private ProcessoDTOResponse converterParaResponse(Processo processo) {
        return ProcessoDTOResponse.builder()
                .numeroProcesso(processo.getNumeroProcesso())
                .dataAjuizamento(DataUtil.formartarDataBruta(processo.getDataAjuizamento()))
                .tribunal(processo.getTribunal())
                .grau(processo.getGrau())
                .sistema(processo.getSistemaNome())//Pega a entity
                .estado(processo.getEstado())
                .tipoDaAcao(processo.getClasseNome())
                .ultimaAtualizacao(DataUtil.formatarDataIso(processo.getUltimaAtualizacao()))
                .assuntos(processo.getAssuntos().stream()
                        .map(a -> new AssuntoDTOResponse(a.getNome()))
                        .collect(Collectors.toList()))
                        .movimentos(processo.getMovimentos().stream()
                        .sorted(Comparator.comparing(Movimento::getDataHora,
                                        Comparator.nullsLast(Comparator.reverseOrder())))
                        .map(m -> new MovimentoDTOResponse(m.getNome(), DataUtil.formatarDataIso
                                (m.getDataHora())))
                        .collect(Collectors.toList()))
                .build();
    }
}
