package com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataJudResponseDTO {
    private long took;
    private HitsWrapperDTO hits;
}
