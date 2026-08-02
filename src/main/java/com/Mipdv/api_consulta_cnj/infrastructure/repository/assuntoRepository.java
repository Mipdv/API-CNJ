package com.Mipdv.api_consulta_cnj.infrastructure.repository;

import com.Mipdv.api_consulta_cnj.infrastructure.entity.Assunto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface assuntoRepository extends JpaRepository<Assunto, Long> {
}
