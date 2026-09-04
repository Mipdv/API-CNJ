package com.Mipdv.api_consulta_cnj.api.response;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.AssuntoCnjDTO;

public class AssuntoCnjDTOFixture {
    public AssuntoCnjDTO build(
             Integer codigo,
             String nome
    ){
        return new AssuntoCnjDTO(codigo, nome);
    }
}
