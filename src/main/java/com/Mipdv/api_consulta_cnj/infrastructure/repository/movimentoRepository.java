package com.Mipdv.api_consulta_cnj.infrastructure.repository;

import com.Mipdv.api_consulta_cnj.infrastructure.entity.Movimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface movimentoRepository extends JpaRepository<Movimento, Long> {

}
