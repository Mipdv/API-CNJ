package com.Mipdv.api_consulta_cnj.business.controller;

import com.Mipdv.api_consulta_cnj.business.service.ProcessoService;
import com.Mipdv.api_consulta_cnj.infrastructure.entity.Processo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //Controller
@RequestMapping("processo")
@RequiredArgsConstructor
public class ProcessoController {

    private final ProcessoService processoService;

    @GetMapping("/{numero}")
    public ResponseEntity<Processo> consultarProcesso(@PathVariable String numero){
        return ResponseEntity.ok(processoService.consultarProcesso(numero));
    }


}
