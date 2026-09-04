package com.Mipdv.api_consulta_cnj.api.response;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.MovimentoCnjDTO;

public class MovimentoCnjDTOFixture {
    public MovimentoCnjDTO build(
    Integer codigo,
    String nome,
    String dataHora
    ){
        return new MovimentoCnjDTO(codigo, nome, dataHora);
    }


}
