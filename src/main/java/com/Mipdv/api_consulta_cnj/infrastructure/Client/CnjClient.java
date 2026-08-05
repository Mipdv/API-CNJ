package com.Mipdv.api_consulta_cnj.infrastructure.Client;

import com.Mipdv.api_consulta_cnj.infrastructure.dtoRequest.ProcessoDTORequest;
import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.DataJudResponseDTO;
import com.Mipdv.api_consulta_cnj.infrastructure.Client.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CNJ", url = "https://api-publica.datajud.cnj.jus.br/",
        configuration = FeignConfig.class)
public interface CnjClient {

    @PostMapping("/api_publica_{tribunal}/_search")
    DataJudResponseDTO consultar(@PathVariable("tribunal") String tribunal,
                                 @RequestBody ProcessoDTORequest request);
}