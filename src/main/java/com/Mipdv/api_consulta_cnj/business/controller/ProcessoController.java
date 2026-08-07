package com.Mipdv.api_consulta_cnj.business.controller;

import com.Mipdv.api_consulta_cnj.business.dtoResponse.ProcessoDTOResponse;
import com.Mipdv.api_consulta_cnj.business.service.ProcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("processo")
@RequiredArgsConstructor
public class ProcessoController {

    private final ProcessoService processoService;

    @PostMapping("/{numero}")
    public ResponseEntity<ProcessoDTOResponse> consultarProcesso(
            @PathVariable String numero) {
        return ResponseEntity.ok(processoService.consultarProcesso(numero));
        //
    }
}