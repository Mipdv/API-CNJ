package com.Mipdv.api_consulta_cnj.api.request;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoRequest.MatchDTO;
import com.Mipdv.api_consulta_cnj.infrastructure.dtoRequest.QueryDTO;

public class QueryDTOFixture {
    public QueryDTO build(
            MatchDTO match
    ){
        return new QueryDTO(match);
    }
}
