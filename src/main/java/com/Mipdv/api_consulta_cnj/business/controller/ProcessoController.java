package com.Mipdv.api_consulta_cnj.business.controller;

import com.Mipdv.api_consulta_cnj.business.dtoResponse.ProcessoDTOResponse;
import com.Mipdv.api_consulta_cnj.business.service.ProcessoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("processo")
@RequiredArgsConstructor
@Tag(name= "Processo_consulta", description = "Consulta processual por meio da PDPJ")
public class ProcessoController {

    private final ProcessoService processoService;

    @PostMapping("/{numero}")
    @Operation(summary = "Consulta processual", description = "Consulta por meio de requisição na API da PDPJ")
    @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Processo não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro de servidor")
    public ResponseEntity<ProcessoDTOResponse> consultarProcesso(
            @PathVariable String numero) {
        return ResponseEntity.ok(processoService.consultarProcesso(numero));
    }
}