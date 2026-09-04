package com.Mipdv.api_consulta_cnj.api.response;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.HitDTO;
import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.ProcessoCnjDTO;

public class HitDTOFixture {
    public HitDTO build(

    String id,

    ProcessoCnjDTO source
    ){return new HitDTO(id, source);
    }
}
