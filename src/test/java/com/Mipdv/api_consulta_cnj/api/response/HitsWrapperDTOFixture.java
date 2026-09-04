package com.Mipdv.api_consulta_cnj.api.response;


import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.HitDTO;
import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.HitsWrapperDTO;

import java.util.List;

public class HitsWrapperDTOFixture {
    public HitsWrapperDTO build(
            List<HitDTO> hits
    ){ return new HitsWrapperDTO(hits);}
}
