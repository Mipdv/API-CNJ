package com.Mipdv.api_consulta_cnj.business.service;

import com.Mipdv.api_consulta_cnj.infrastructure.Client.CnjClient;
import com.Mipdv.api_consulta_cnj.infrastructure.entity.Processo;
import com.Mipdv.api_consulta_cnj.infrastructure.exceptions.ConflictException;
import com.Mipdv.api_consulta_cnj.infrastructure.repository.processoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProcessoService {

    //Injeção de dependêcia
    private final processoRepository processoRepository;
    private final CnjClient cnjClient;

    public Processo consultarProcesso(String numero){
        Optional<Processo> cache = processoRepository.findByNumeroProcesso(numero);
        if(cache.isPresent()){

            Processo processo = cache.get();
            if(processo.getDataConsulta() != null &&//tratativa de nullPointerException
                    processo.getDataConsulta().isAfter(LocalDateTime.now().minusHours(24))){
                return processo;
            }
            Processo atualizado = cnjClient.consultar(numero);
            atualizado.setId(processo.getId());
            return salvarConsulta(atualizado);//Salvar cache diretamente no DB com tempo de 24h
        }
        Processo processo = cnjClient.consultar(numero);
        return salvarConsulta(processo);

    }
    public Processo salvarConsulta(Processo processo){
        processo.setDataConsulta(LocalDateTime.now());
        return processoRepository.save(processo);
    }

    private Processo consultarApiCnj(String numero){
        try {
            return cnjClient.consultar(numero);
        }catch (Exception e){
            throw new ConflictException("Erro ao consultar o processo", e);
        }
    }

}
