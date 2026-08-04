package com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HitsWrapperDTO {
    private List<HitDTO> hits;
}
