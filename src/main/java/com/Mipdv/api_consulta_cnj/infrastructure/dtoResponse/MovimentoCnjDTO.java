package com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovimentoCnjDTO {
    private Integer codigo;
    private String nome;
    private String dataHora;


}
