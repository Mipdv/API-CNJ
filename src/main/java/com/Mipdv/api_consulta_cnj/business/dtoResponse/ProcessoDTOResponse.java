package com.Mipdv.api_consulta_cnj.business.dtoResponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessoDTOResponse {
    private String numeroProcesso;

    private String tribunal;

    private String classeNome;

    private String ultimaAtualizacao;

}
