package com.Mipdv.api_consulta_cnj.api.request;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoRequest.ProcessoDTORequest;
import com.Mipdv.api_consulta_cnj.infrastructure.dtoRequest.QueryDTO;

public class ProcessoDTORequestFixture {
    public ProcessoDTORequest build(
            QueryDTO query
    ){
        return new ProcessoDTORequest(query);
    }
}
