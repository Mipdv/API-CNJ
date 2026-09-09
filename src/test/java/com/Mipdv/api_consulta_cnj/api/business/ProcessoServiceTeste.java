package com.Mipdv.api_consulta_cnj.api.business;


import com.Mipdv.api_consulta_cnj.api.response.*;
import com.Mipdv.api_consulta_cnj.business.dtoResponse.ProcessoDTOResponse;
import com.Mipdv.api_consulta_cnj.business.service.ProcessoService;
import com.Mipdv.api_consulta_cnj.infrastructure.Client.CnjClient;
import com.Mipdv.api_consulta_cnj.infrastructure.dtoRequest.ProcessoDTORequest;
import com.Mipdv.api_consulta_cnj.infrastructure.dtoResponse.*;
import com.Mipdv.api_consulta_cnj.infrastructure.entity.Assunto;
import com.Mipdv.api_consulta_cnj.infrastructure.entity.Processo;
import com.Mipdv.api_consulta_cnj.infrastructure.exceptions.ConflictException;
import com.Mipdv.api_consulta_cnj.infrastructure.repository.assuntoRepository;
import com.Mipdv.api_consulta_cnj.infrastructure.repository.processoRepository;
import com.Mipdv.api_consulta_cnj.infrastructure.util.TribunalInfo;
import com.Mipdv.api_consulta_cnj.infrastructure.util.TribunalResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessoServiceTeste {

    @InjectMocks
    private ProcessoService processoService;

    @Mock//Dependências REAIS da service
    private processoRepository processoRepository;
    @Mock
    private assuntoRepository assuntoRepository;
    @Mock
    private CnjClient cnjClient;
    @Mock
    private TribunalResolver tribunalResolver;
    //O que não tiver, é apenas DADOS

    private TribunalInfo tribunalInfo;//Precisa de new
    private ProcessoCnjDTO processoCnjDTO;
    private DataJudResponseDTO dataJudResponseDTO;
    private Processo processo;
    private HitDTO hitDTO;
    private HitsWrapperDTO hitsWrapperDTO;
    private AssuntoCnjDTO assuntoCnjDTO;
    String numeroBruto;
    String numeroLimpo;




    @BeforeEach
    public void setup() {
        //Necessario para não quebrar com NPE
        tribunalInfo = new TribunalInfo("trf5", "PB");
        numeroBruto = "0819283-90.2023.8.15.2001";
        numeroLimpo = numeroBruto.replaceAll("[^0-9]", "");
        processo = new ProcessoFixture().build(1L, numeroLimpo, "1", "trf5", "PB", null,
                "2023-08-15", "2024-01-10", 198, "Procedimento Comum", "PJe", null,
                LocalDateTime.now().minusHours(1), new ArrayList<>(), new ArrayList<>());
        //Processo vindo da api datajud
        processoCnjDTO = new ProcessoCnjDTOFixture().build(numeroLimpo,
                "trf5", null, "1", new SistemaDTOFixture().build(1, "PJe"),
                new ClasseDTOFixture().build(198, "Procedimento Comum"), null,
                new ArrayList<>(), new ArrayList<>(), "2024-01-10T09:00:00.000Z",
                "2023-08-15T10:00:00.000Z");

        //Empacota o processoCnjDTO
        hitDTO = new HitDTOFixture().build("hit-1", processoCnjDTO);

        hitsWrapperDTO = new HitsWrapperDTOFixture().build(new ArrayList<>(List.of(hitDTO)));
        dataJudResponseDTO = new DataJudResponseDTOFixture().build(2350L, hitsWrapperDTO);



    }
    @Test
    void deveRetornarConsultaComSucesso() {
        when(tribunalResolver.resolver(anyString())).thenReturn(tribunalInfo);
//        when(processoRepository.save(processo)).thenReturn(processo);

        when(processoRepository.findByNumeroProcesso(numeroLimpo)).
                thenReturn(Optional.of(processo));

        ProcessoDTOResponse response = processoService.consultarProcesso(numeroBruto);

        assertNotNull(response);
        assertEquals(numeroLimpo,response.getNumeroProcesso());
        assertEquals("PB", response.getEstado());
        verify(processoRepository).findByNumeroProcesso(numeroLimpo);
        }
    @Test
    void deveConsultarApiQuandoCacheNaoEncontrado(){//Nao achou nada no cache
        //Stub cache vazio
        when(tribunalResolver.resolver(anyString())).thenReturn(tribunalInfo);
        when(processoRepository.findByNumeroProcesso(numeroLimpo)).thenReturn(
                Optional.empty());
        //Deixei explicito o ProcessoDTORequest.class para legibilidade.
        when(cnjClient.consultar(anyString(),any(ProcessoDTORequest.class))).thenReturn(dataJudResponseDTO);
        when(processoRepository.save(any(Processo.class))).thenAnswer(invocation ->
                invocation.getArgument(0));


        //o número bruto que entra e depois fica limpo
        ProcessoDTOResponse response = processoService.consultarProcesso(numeroBruto);


        assertNotNull(response);
        assertEquals(numeroLimpo,response.getNumeroProcesso());
        assertEquals("trf5", response.getTribunal());
        verify(cnjClient, times(1)).consultar(anyString(), any());
        verify(processoRepository, times(1)).save(any(Processo.class));
    }

    @Test
    void naoDeveSalvarProcesso(){
        when(tribunalResolver.resolver(anyString())).thenReturn(tribunalInfo);
        when(processoRepository.findByNumeroProcesso(numeroLimpo)).thenReturn(
                Optional.of(processo));

        ProcessoDTOResponse response = processoService.consultarProcesso(numeroBruto);

        assertNotNull(response);
        verify(processoRepository,never()).save(any(Processo.class));
        verify(cnjClient, never()).consultar(anyString(), any());

    }

    @Test
    void deveAtualizarQuandoCacheMaiorQue24Horas(){
        processo.setDataConsulta(LocalDateTime.now().minusHours(25));
        when(tribunalResolver.resolver(anyString())).thenReturn(tribunalInfo);
        when(processoRepository.findByNumeroProcesso(numeroLimpo)).thenReturn(Optional.of(
                processo));
        when(cnjClient.consultar(anyString(),any())).thenReturn(dataJudResponseDTO);
        when(processoRepository.save(processo)).thenReturn(processo);

        ProcessoDTOResponse response = processoService.consultarProcesso(numeroBruto);

        assertNotNull(response);
        verify(cnjClient, times(1)).consultar(anyString(),any());
        verify(processoRepository, times(1)).save(processo);
    }
    @Test
    void deveLancarConflictExceptionQuandoApiNaoEncontrarProcesso() {
        when(tribunalResolver.resolver(anyString())).thenReturn(tribunalInfo);
        when(processoRepository.findByNumeroProcesso(numeroLimpo)).thenReturn(
                Optional.empty());

        HitsWrapperDTO hitsWrapperDTOVazio = new HitsWrapperDTOFixture().build(new ArrayList<>());
        DataJudResponseDTO dataJudResponseDTOVazio = new DataJudResponseDTOFixture().build(0L, hitsWrapperDTOVazio);

        when(cnjClient.consultar(anyString(), any())).thenReturn(dataJudResponseDTOVazio);
        assertThrows(ConflictException.class, () -> processoService.consultarProcesso(numeroBruto));
        verify(processoRepository,never()).save(any());
    }

    @Test
    void deveLancarExcecaoCasoErroGenerico(){
        when(tribunalResolver.resolver(anyString())).thenReturn(tribunalInfo);
        when(processoRepository.findByNumeroProcesso(numeroLimpo)).thenReturn(
                Optional.empty());

        //Sem retorno de nada - exceção estoura antes

        when(cnjClient.consultar(anyString(),any())).thenThrow(new RuntimeException("timeout"));
        assertThrows(ConflictException.class, () -> processoService.consultarProcesso(numeroBruto));
        verify(processoRepository,never()).save(any());
    }

    @Test
    void deveRetornarAssuntoJáExistente(){
        assuntoCnjDTO = new AssuntoCnjDTOFixture().build(1, "Direito Civil");
        processoCnjDTO = new ProcessoCnjDTOFixture().build(numeroLimpo,
                "trf5", null, "1", new SistemaDTOFixture().build(1, "PJe"),
                new ClasseDTOFixture().build(198, "Procedimento Comum"), null,
                new ArrayList<>(List.of(assuntoCnjDTO)), new ArrayList<>(), "2024-01-10T09:00:00.000Z",
                "2023-08-15T10:00:00.000Z");
        hitDTO = new HitDTOFixture().build("hit-1", processoCnjDTO);
        hitsWrapperDTO = new HitsWrapperDTOFixture().build(new ArrayList<>(List.of(hitDTO)));
        dataJudResponseDTO = new DataJudResponseDTOFixture().build(2350L, hitsWrapperDTO);
        Assunto assunto = new AssuntoFixture().build(1L, "Direito Civil", new ArrayList<>());
        when(tribunalResolver.resolver(anyString())).thenReturn(tribunalInfo);
        processo.setDataConsulta(LocalDateTime.now().minusHours(25));
        when(processoRepository.findByNumeroProcesso(numeroLimpo)).thenReturn(Optional.of(processo));
        when(processoRepository.save(processo)).thenReturn(processo);
        when(cnjClient.consultar(anyString(),any())).thenReturn(dataJudResponseDTO);
        when(assuntoRepository.findByNome(assuntoCnjDTO.getNome())).thenReturn(Optional.of(assunto));

        ProcessoDTOResponse response = processoService.consultarProcesso(numeroBruto);

        assertNotNull(response);
        verify(cnjClient, times(1)).consultar(anyString(), any());
        assertEquals("Direito Civil", response.getAssuntos().get(0).getNome());
        verify(assuntoRepository,never()).save(any());
    }
    @Test
    void deveCriarAssuntoNaoExistente(){
        processo.setDataConsulta(LocalDateTime.now().minusHours(25));
        assuntoCnjDTO = new AssuntoCnjDTOFixture().build(1, "Direito Civil");
        processoCnjDTO = new ProcessoCnjDTOFixture().build(numeroLimpo,
                "trf5", null, "1", new SistemaDTOFixture().build(1, "PJe"),
                new ClasseDTOFixture().build(198, "Procedimento Comum"), null,
                new ArrayList<>(List.of(assuntoCnjDTO)), new ArrayList<>(), "2024-01-10T09:00:00.000Z",
                "2023-08-15T10:00:00.000Z");
        hitDTO = new HitDTOFixture().build("hit-1", processoCnjDTO);
        hitsWrapperDTO = new HitsWrapperDTOFixture().build(new ArrayList<>(List.of(hitDTO)));
        dataJudResponseDTO = new DataJudResponseDTOFixture().build(2350L, hitsWrapperDTO);
        Assunto assunto = new AssuntoFixture().build(1L, "Direito Civil", new ArrayList<>());
        when(assuntoRepository.findByNome(assuntoCnjDTO.getNome())).thenReturn(Optional.empty());
        //assunto é o criado via fixture.
        when(assuntoRepository.save(any(Assunto.class))).thenAnswer//Assunto.class = novo da service
                (invocation -> invocation.getArgument(0));
        when(tribunalResolver.resolver(anyString())).thenReturn(tribunalInfo);
        processo.setDataConsulta(LocalDateTime.now().minusHours(25));
        when(processoRepository.findByNumeroProcesso(numeroLimpo)).thenReturn(Optional.of(processo));
        when(processoRepository.save(any(Processo.class))).thenAnswer(invocation ->
                invocation.getArgument(0));
        when(cnjClient.consultar(anyString(),any())).thenReturn(dataJudResponseDTO);

        ProcessoDTOResponse response = processoService.consultarProcesso(numeroBruto);

        assertNotNull(response);
        verify(cnjClient, times(1)).consultar(anyString(), any());
        assertEquals("Direito Civil", response.getAssuntos().get(0).getNome());
        //any(Assunto.class) - utilizei para legibilidade.
        verify(assuntoRepository,times(1)).save(any(Assunto.class));
    }

    @Test
    void deveSubstituirOAssuntoAnterior(){
        processo.setDataConsulta(LocalDateTime.now().minusHours(25));
        Assunto assunto = new AssuntoFixture().build(2L, "Direito Penal", new ArrayList<>());
        processo.setAssuntos(new ArrayList<>(List.of(assunto)));
        assuntoCnjDTO = new AssuntoCnjDTOFixture().build(1, "Direito Civil");
        processoCnjDTO = new ProcessoCnjDTOFixture().build(numeroLimpo,
                "trf5", null, "1", new SistemaDTOFixture().build(1, "PJe"),
                new ClasseDTOFixture().build(198, "Procedimento Comum"), null,
                new ArrayList<>(List.of(assuntoCnjDTO)), new ArrayList<>(), "2024-01-10T09:00:00.000Z",
                "2023-08-15T10:00:00.000Z");
        hitDTO = new HitDTOFixture().build("hit-1", processoCnjDTO);
        hitsWrapperDTO = new HitsWrapperDTOFixture().build(new ArrayList<>(List.of(hitDTO)));
        dataJudResponseDTO = new DataJudResponseDTOFixture().build(2350L, hitsWrapperDTO);
        when(assuntoRepository.findByNome(assuntoCnjDTO.getNome())).thenReturn(Optional.empty());
        when(tribunalResolver.resolver(anyString())).thenReturn(tribunalInfo);
        when(processoRepository.save(processo)).thenReturn(processo);
        when(processoRepository.findByNumeroProcesso(numeroLimpo)).thenReturn(Optional.of(processo));
        when(cnjClient.consultar(anyString(), any())).thenReturn(dataJudResponseDTO);

        ProcessoDTOResponse response = processoService.consultarProcesso(numeroBruto);

        assertNotNull(response);
        assertEquals(1, response.getAssuntos().size());
        assertEquals("Direito Civil", response.getAssuntos().get(0).getNome());


    }
}