package com.Mipdv.api_consulta_cnj.api.business.dtoResponse;

import com.Mipdv.api_consulta_cnj.business.dtoResponse.AssuntoDTOResponse;
import com.Mipdv.api_consulta_cnj.business.dtoResponse.MovimentoDTOResponse;
import com.Mipdv.api_consulta_cnj.business.dtoResponse.ProcessoDTOResponse;

import java.util.List;

public class ProcessoDTOResponseFixture {
    public ProcessoDTOResponse build(
            String numeroProcesso,
            String dataAjuizamento,
            String tribunal,
            String grau,
            String sistema,
            String estado,
            String tipoDaAcao,//classeNome
            String ultimaAtualizacao,
            List<AssuntoDTOResponse> assuntos,
            List<MovimentoDTOResponse> movimentos
    ) {
        return new ProcessoDTOResponse(numeroProcesso, dataAjuizamento, tribunal, grau, sistema,
                estado, tipoDaAcao, ultimaAtualizacao, assuntos, movimentos);
    }
}