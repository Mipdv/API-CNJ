package com.Mipdv.api_consulta_cnj.api.util;

import com.Mipdv.api_consulta_cnj.infrastructure.util.TribunalInfo;

public class TribunalInfoFixture {
    public TribunalInfo build(
            String alias,
            String estado
    ){
        return new TribunalInfo(alias, estado);
    }
}
