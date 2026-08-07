// TribunalResolver.java
package com.Mipdv.api_consulta_cnj.infrastructure.util;

import com.Mipdv.api_consulta_cnj.infrastructure.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TribunalResolver {

    // chave = "J.TR" (segmento + tribunal), valor = alias do índice no Datajud
    private static final Map<String, String> ALIASES = Map.ofEntries(
            // === TRIBUNAIS SUPERIORES
            Map.entry("9.01", "stf"),
            Map.entry("9.02", "cnj"),
            Map.entry("9.03", "stj"),
            Map.entry("9.04", "stm"),
            Map.entry("9.05", "tse"),
            Map.entry("9.06", "tst"),
            // === JUSTIÇA FEDERAL
            Map.entry("4.01", "trf1"),
            Map.entry("4.02", "trf2"),
            Map.entry("4.03", "trf3"),
            Map.entry("4.04", "trf4"),
            Map.entry("4.05", "trf5"),
            Map.entry("4.06", "trf6"),
            // === JUSTIÇA ESTADUAL
            Map.entry("8.01", "tjac"),
            Map.entry("8.02", "tjal"),
            Map.entry("8.03", "tjam"),
            Map.entry("8.04", "tjap"),
            Map.entry("8.05", "tjba"),
            Map.entry("8.06", "tjce"),
            Map.entry("8.07", "tjdft"),
            Map.entry("8.08", "tjes"),
            Map.entry("8.09", "tjgo"),
            Map.entry("8.10", "tjma"),
            Map.entry("8.11", "tjmt"),
            Map.entry("8.12", "tjms"),
            Map.entry("8.13", "tjmg"),
            Map.entry("8.14", "tjpa"),
            Map.entry("8.15", "tjpb"),
            Map.entry("8.16", "tjpr"),
            Map.entry("8.17", "tjpe"),
            Map.entry("8.18", "tjpi"),
            Map.entry("8.19", "tjrj"),
            Map.entry("8.20", "tjrn"),
            Map.entry("8.21", "tjrs"),
            Map.entry("8.22", "tjro"),
            Map.entry("8.23", "tjrr"),
            Map.entry("8.24", "tjsc"),
            Map.entry("8.25", "tjsp"),
            Map.entry("8.26", "tjse"),
            Map.entry("8.27", "tjto"),
            // === JUSTIÇA DO TRABALHO
            Map.entry("5.01", "trt1"),
            Map.entry("5.02", "trt2"),
            Map.entry("5.03", "trt3"),
            Map.entry("5.04", "trt4"),
            Map.entry("5.05", "trt5"),
            Map.entry("5.06", "trt6"),
            Map.entry("5.07", "trt7"),
            Map.entry("5.08", "trt8"),
            Map.entry("5.09", "trt9"),
            Map.entry("5.10", "trt10"),
            Map.entry("5.11", "trt11"),
            Map.entry("5.12", "trt12"),
            Map.entry("5.13", "trt13"),
            Map.entry("5.14", "trt14"),
            Map.entry("5.15", "trt15"),
            Map.entry("5.16", "trt16"),
            Map.entry("5.17", "trt17"),
            Map.entry("5.18", "trt18"),
            Map.entry("5.19", "trt19"),
            Map.entry("5.20", "trt20"),
            Map.entry("5.21", "trt21"),
            Map.entry("5.22", "trt22"),
            Map.entry("5.23", "trt23"),
            Map.entry("5.24", "trt24"),
            // === JUSTIÇA ELEITORAL
            Map.entry("6.01", "tre-ac"),
            Map.entry("6.02", "tre-al"),
            Map.entry("6.03", "tre-am"),
            Map.entry("6.04", "tre-ap"),
            Map.entry("6.05", "tre-ba"),
            Map.entry("6.06", "tre-ce"),
            Map.entry("6.07", "tre-df"),
            Map.entry("6.08", "tre-es"),
            Map.entry("6.09", "tre-go"),
            Map.entry("6.10", "tre-ma"),
            Map.entry("6.11", "tre-mt"),
            Map.entry("6.12", "tre-ms"),
            Map.entry("6.13", "tre-mg"),
            Map.entry("6.14", "tre-pa"),
            Map.entry("6.15", "tre-pb"),
            Map.entry("6.16", "tre-pr"),
            Map.entry("6.17", "tre-pe"),
            Map.entry("6.18", "tre-pi"),
            Map.entry("6.19", "tre-rj"),
            Map.entry("6.20", "tre-rn"),
            Map.entry("6.21", "tre-rs"),
            Map.entry("6.22", "tre-ro"),
            Map.entry("6.23", "tre-rr"),
            Map.entry("6.24", "tre-sc"),
            Map.entry("6.25", "tre-sp"),
            Map.entry("6.26", "tre-se"),
            Map.entry("6.27", "tre-to"),
            // === JUSTIÇA MILITAR ESTADUAL
            Map.entry("9.13", "tjmmg"),
            Map.entry("9.21", "tjmrs"),
            Map.entry("9.25", "tjmsp")

    );

    public String resolver(String numeroProcesso) {
        if (numeroProcesso == null || !numeroProcesso.matches("\\d{20}")) {
            throw new BadRequestException("Número do processo deve conter exatamente 20 dígitos");
        }
        // NNNNNNN DD AAAA J TR OOOO -> posições 13 (J) e 14-15 (TR), 0-indexed
        String j = numeroProcesso.substring(13, 14);
        String tr = numeroProcesso.substring(14, 16);
        String chave = j + "." + tr;

        String alias = ALIASES.get(chave);
        if (alias == null) {
            throw new BadRequestException("Tribunal não mapeado para o segmento: " + chave);
        }
        return alias;
    }
}