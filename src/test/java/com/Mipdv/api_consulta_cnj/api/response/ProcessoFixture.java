package com.Mipdv.api_consulta_cnj.api.response;

import com.Mipdv.api_consulta_cnj.infrastructure.entity.Assunto;
import com.Mipdv.api_consulta_cnj.infrastructure.entity.Movimento;
import com.Mipdv.api_consulta_cnj.infrastructure.entity.Processo;

import java.time.LocalDateTime;
import java.util.List;

public class ProcessoFixture {
    public Processo build(
            Long id,
            String numeroProcesso,
            String grau,
            String tribunal,
            String estado,
            String sigilo,
            String dataAjuizamento,
            String ultimaAtualizacao,
            Integer codigo,
            String classeNome,
            String sistemaNome,
            String formatoNome,
            LocalDateTime dataConsulta,
            List<Movimento> movimentos,
            List<Assunto> assuntos

    ) {
        return new Processo(id, numeroProcesso,grau, tribunal, estado, sigilo, dataAjuizamento, ultimaAtualizacao,
                codigo, classeNome, sistemaNome, formatoNome, dataConsulta, movimentos, assuntos);
    }
}