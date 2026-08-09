package com.Mipdv.api_consulta_cnj.business.dtoResponse;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MovimentoDTOResponse {
    private String nomeDoAto;
    private String dataHora;
}