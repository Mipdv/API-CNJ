package com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrgaoJulgadorDTO {
    private String nome;
    private String codigoMunicipioIBGE;
}
