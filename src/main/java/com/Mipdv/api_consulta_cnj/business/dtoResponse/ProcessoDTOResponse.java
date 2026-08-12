package com.Mipdv.api_consulta_cnj.business.dtoResponse;

import lombok.*;
import java.util.List;

//JSON Response
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProcessoDTOResponse {
    private String numeroProcesso;
    private String dataAjuizamento;
    private String tribunal;
    private String grau;
    private String sistema;
    private String estado;
    private String TipoDaAcao;//classeNome
    private String ultimaAtualizacao;
    private List<AssuntoDTOResponse> assuntos;
    private List<MovimentoDTOResponse> movimentos;
}