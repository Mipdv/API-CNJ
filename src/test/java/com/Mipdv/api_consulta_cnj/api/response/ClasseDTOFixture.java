package com.Mipdv.api_consulta_cnj.api.response;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.ClasseDTO;

public class ClasseDTOFixture {
    public ClasseDTO build(
            Integer codigo,
            String nome
    ){
        return new ClasseDTO(codigo, nome);
    }
}
