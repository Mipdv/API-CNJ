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
    private String nome;
    private String grau;
    private SistemaDTO sistema;//Pega o sistema+nome - conforme JSON datajud
    private ClasseDTO classe;
    private OrgaoJulgadorDTO orgaoJulgador;
    private List<AssuntoCnjDTO> assuntos;
    private List<MovimentoCnjDTO> movimentos;
    private String dataHoraUltimaAtualizacao;
    private String dataAjuizamento;
}
