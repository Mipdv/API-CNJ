package com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HitDTO {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("_source")
    private ProcessoCnjDTO source;
}
