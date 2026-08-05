package com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessoCnjDTO {
    private String numeroProcesso;
    private String tribunal;
    private String grau;
    private ClasseDTO classe;
    private OrgaoJulgadorDTO orgaoJulgador;
    private List<AssuntoCnjDTO> assuntos;
    private List<MovimentoCnjDTO> movimentos;
    private String dataHoraUltimaAtualizacao;
    private String dataAjuizamento;

}
