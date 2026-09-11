package com.Mipdv.api_consulta_cnj.api;


import com.Mipdv.api_consulta_cnj.api.business.dtoResponse.ProcessoDTOResponseFixture;
import com.Mipdv.api_consulta_cnj.business.controller.ProcessoController;
import com.Mipdv.api_consulta_cnj.business.dtoResponse.ProcessoDTOResponse;
import com.Mipdv.api_consulta_cnj.business.service.ProcessoService;
import com.Mipdv.api_consulta_cnj.infrastructure.exceptions.ConflictException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessoControllerTeste {
    @InjectMocks
    private ProcessoController processoController;
    @Mock
    private ProcessoService processoService;
    private ProcessoDTOResponse processoDTOResponse;

    @BeforeEach
    public void setup(){
        processoDTOResponse = new ProcessoDTOResponseFixture().build("0036336-54.2026.4.05.8200", "09/09/2026",
                "tribunal", "1", "Pje", "PB", "Previdenciario", "09/09/2026",
        new ArrayList<>(), new ArrayList<>());
    }

    @Test
    void deveRetornarProcessoComSucesso(){
        when(processoService.consultarProcesso(processoDTOResponse.getNumeroProcesso())).thenReturn(processoDTOResponse);

        ResponseEntity<ProcessoDTOResponse> response = processoController.consultarProcesso(processoDTOResponse.getNumeroProcesso());

        verify(processoService).consultarProcesso("0036336-54.2026.4.05.8200");
        verifyNoMoreInteractions(processoService);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(processoDTOResponse, response.getBody());
    }
    @Test
    void naoDeveRetornarProcessoComSucesso(){
        when(processoService.consultarProcesso(processoDTOResponse.getNumeroProcesso())).
                thenThrow(new ConflictException("Processo não encontrado"));

        assertThrows(ConflictException.class, () -> processoController.consultarProcesso(processoDTOResponse.getNumeroProcesso()));
        verify(processoService, times(1)).consultarProcesso(anyString());
    }
    @Test
    void deveRetornarBodyNuloQuandoServiceRetornarNull() {
        when(processoService.consultarProcesso(anyString())).thenReturn(null);

        ResponseEntity<ProcessoDTOResponse> response =
                processoController.consultarProcesso(processoDTOResponse.getNumeroProcesso());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}
