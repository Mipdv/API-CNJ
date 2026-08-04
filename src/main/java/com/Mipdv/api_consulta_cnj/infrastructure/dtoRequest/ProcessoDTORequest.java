package com.Mipdv.api_consulta_cnj.infrastructure.dtoRequest;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProcessoDTORequest {
    private QueryDTO query;

    public static ProcessoDTORequest porNumeroProcesso(String numeroProcesso) {
        return ProcessoDTORequest.builder()
                .query(QueryDTO.builder()
                        .match(new MatchDTO(numeroProcesso))
                        .build())
                .build();
    }
}