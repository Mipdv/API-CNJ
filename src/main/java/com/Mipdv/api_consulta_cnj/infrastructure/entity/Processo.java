package com.Mipdv.api_consulta_cnj.infrastructure.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Consulta")
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "numero_processo", length = 20)
    private String numeroProcesso;
    private String grau;
    @Column(name = "tribunal", length = 10)
    private String tribunal;
    @Column(name = "Sigilo")
    private String sigilo;
    @Column(name = "data_ajuizamento")
    private String dataAjuizamento;
    @Column(name = "ultima_atualizacao")
    private String ultimaAtualizacao;
    @Column(name = "classe_codigo")
    private Integer codigo;
    @Column(name = "classe_nome")
    private String classeNome;
    @Column(name = "sistema_nome")
    private String sistemaNome;
    @Column(name = "formato_nome")
    private String formatoNome;
    @Column(name = "data_consulta")
    private LocalDateTime dataConsulta;
    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimento> movimentos;
    @ManyToMany
    @JoinTable(
            name = "processo_assunto",
            joinColumns = @JoinColumn(name = "processo_id"),
            inverseJoinColumns = @JoinColumn(name = "assunto_id")
    )
    private List<Assunto> assuntos;

}
