package com.Mipdv.api_consulta_cnj.api.response;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.SistemaDTO;

public class SistemaDTOFixture {
    public SistemaDTO build(
    Integer codigo,
    String nome
    ){
        return new SistemaDTO(codigo, nome);
    }

}
