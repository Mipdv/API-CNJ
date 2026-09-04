package com.Mipdv.api_consulta_cnj.api.response;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.*;
import lombok.*;

import java.util.List;

public class ProcessoCnjDTOFixture {
    public ProcessoCnjDTO build(
    String numeroProcesso,
    String tribunal,
    String nome,
    String grau,
    SistemaDTO sistema,
    ClasseDTO classe,
    OrgaoJulgadorDTO orgaoJulgador,
    List<AssuntoCnjDTO> assuntos,
    List<MovimentoCnjDTO> movimentos,
    String dataHoraUltimaAtualizacao,
    String dataAjuizamento
            ){return new ProcessoCnjDTO(numeroProcesso, tribunal, nome, grau, sistema, classe, orgaoJulgador,
            assuntos, movimentos,
            dataHoraUltimaAtualizacao, dataAjuizamento);}
}
