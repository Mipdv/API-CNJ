package com.Mipdv.api_consulta_cnj.infrastructure.Client;


import com.Mipdv.api_consulta_cnj.infrastructure.entity.Processo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CNJ", url = "https://api-publica.datajud.cnj.jus.br/ ")
public interface CnjClient {
    @GetMapping("/processos/{numero}")
    Processo consultar(@PathVariable String numero);

}
