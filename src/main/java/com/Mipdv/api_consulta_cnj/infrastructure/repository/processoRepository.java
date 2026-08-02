package com.Mipdv.api_consulta_cnj.infrastructure.repository;

import com.Mipdv.api_consulta_cnj.infrastructure.entity.Processo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface processoRepository extends JpaRepository<Processo , Long> {

    Optional<Processo> findByNumeroProcesso(String numeroProcesso);
}
