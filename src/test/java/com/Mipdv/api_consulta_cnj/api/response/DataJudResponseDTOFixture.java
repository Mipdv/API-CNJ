package com.Mipdv.api_consulta_cnj.api.response;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.DataJudResponseDTO;
import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.HitsWrapperDTO;

public class DataJudResponseDTOFixture {
    public DataJudResponseDTO build(
    long took,
    HitsWrapperDTO hits
    ) {
        return new DataJudResponseDTO(took, hits);
    }
}
