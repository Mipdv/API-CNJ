package com.Mipdv.api_consulta_cnj.api.response;

import com.Mipdv.api_consulta_cnj.infrastructure.entity.Movimento;
import com.Mipdv.api_consulta_cnj.infrastructure.entity.Processo;

public class MovimentoFixture {
    public Movimento build(
            long Id,
            Integer codigo,
            String nome,
            String dataHora,
            Processo processo
    ){
        return new Movimento(Id, codigo, nome, dataHora, processo);
    }
}
