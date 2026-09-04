package com.Mipdv.api_consulta_cnj.api.response;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.OrgaoJulgadorDTO;

public class OrgaoJulgadorDTOFixture {
    public OrgaoJulgadorDTO build(
    String nome,
    String codigoMunicipioIBGE
    ){
        return new OrgaoJulgadorDTO(nome,codigoMunicipioIBGE);
    }
}
