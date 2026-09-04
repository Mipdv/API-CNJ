package com.Mipdv.api_consulta_cnj.api.business.dtoResponse;

import com.Mipdv.api_consulta_cnj.business.dtoResponse.MovimentoDTOResponse;

public class MovimentoDTOResponseFixture {
    public MovimentoDTOResponse build(
    String nomeDoAto,
    String dataHora
    ){
        return new MovimentoDTOResponse(nomeDoAto, dataHora);
    }
}