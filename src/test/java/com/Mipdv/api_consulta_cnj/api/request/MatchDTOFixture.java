package com.Mipdv.api_consulta_cnj.api.request;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoRequest.MatchDTO;

public class MatchDTOFixture {
    public MatchDTO build(
            String numeroProcesso

    ){
        return new MatchDTO(numeroProcesso);
    }
}
