package com.Mipdv.api_consulta_cnj.api.business.dtoResponse;


import com.Mipdv.api_consulta_cnj.business.dtoResponse.AssuntoDTOResponse;

public class AssuntoDTOResponseFixture {
    public AssuntoDTOResponse build(
    String nome
    ){
        return new AssuntoDTOResponse(nome);
    }
}
