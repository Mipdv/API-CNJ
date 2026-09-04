package com.Mipdv.api_consulta_cnj.api.response;

import com.Mipdv.api_consulta_cnj.infrastructure.entity.Assunto;
import com.Mipdv.api_consulta_cnj.infrastructure.entity.Processo;

import java.util.List;

public class AssuntoFixture {
    public Assunto build(
            long Id,
            String nome,
            List<Processo> processos
    ){
        return new Assunto(Id, nome, processos);
    }
}
