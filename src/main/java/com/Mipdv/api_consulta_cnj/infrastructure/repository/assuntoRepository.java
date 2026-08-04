package com.Mipdv.api_consulta_cnj.infrastructure.repository;

import com.Mipdv.api_consulta_cnj.infrastructure.entity.Assunto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface assuntoRepository extends JpaRepository<Assunto, Long> {
    Optional<Assunto> findByNome(String nome);
}
