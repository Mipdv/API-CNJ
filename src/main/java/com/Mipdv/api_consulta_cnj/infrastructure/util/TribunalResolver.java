package com.Mipdv.api_consulta_cnj.infrastructure.util;

import com.Mipdv.api_consulta_cnj.infrastructure.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TribunalResolver {

    // chave = "J.TR"
    private static final Map<String, TribunalInfo> TRIBUNAIS = Map.ofEntries(
            // === TRIBUNAIS SUPERIORES
            Map.entry("9.01", new TribunalInfo("stf", "DF")),
            Map.entry("9.02", new TribunalInfo("cnj", "DF")),
            Map.entry("9.03", new TribunalInfo("stj", "DF")),
            Map.entry("9.04", new TribunalInfo("stm", "DF")),
            Map.entry("9.05", new TribunalInfo("tse", "DF")),
            Map.entry("9.06", new TribunalInfo("tst", "DF")),

            // === JUSTIÇA FEDERAL
            Map.entry("4.01", new TribunalInfo("trf1", "DF")),
            Map.entry("4.02", new TribunalInfo("trf2", "RJ")),
            Map.entry("4.03", new TribunalInfo("trf3", "SP")),
            Map.entry("4.04", new TribunalInfo("trf4", "RS")),
            Map.entry("4.05", new TribunalInfo("trf5", "PE")),
            Map.entry("4.06", new TribunalInfo("trf6", "MG")),

            // === JUSTIÇA ESTADUAL
            Map.entry("8.01", new TribunalInfo("tjac", "AC")),
            Map.entry("8.02", new TribunalInfo("tjal", "AL")),
            Map.entry("8.03", new TribunalInfo("tjam", "AM")),
            Map.entry("8.04", new TribunalInfo("tjap", "AP")),
            Map.entry("8.05", new TribunalInfo("tjba", "BA")),
            Map.entry("8.06", new TribunalInfo("tjce", "CE")),
            Map.entry("8.07", new TribunalInfo("tjdft", "DF")),
            Map.entry("8.08", new TribunalInfo("tjes", "ES")),
            Map.entry("8.09", new TribunalInfo("tjgo", "GO")),
            Map.entry("8.10", new TribunalInfo("tjma", "MA")),
            Map.entry("8.11", new TribunalInfo("tjmt", "MT")),
            Map.entry("8.12", new TribunalInfo("tjms", "MS")),
            Map.entry("8.13", new TribunalInfo("tjmg", "MG")),
            Map.entry("8.14", new TribunalInfo("tjpa", "PA")),
            Map.entry("8.15", new TribunalInfo("tjpb", "PB")),
            Map.entry("8.16", new TribunalInfo("tjpr", "PR")),
            Map.entry("8.17", new TribunalInfo("tjpe", "PE")),
            Map.entry("8.18", new TribunalInfo("tjpi", "PI")),
            Map.entry("8.19", new TribunalInfo("tjrj", "RJ")),
            Map.entry("8.20", new TribunalInfo("tjrn", "RN")),
            Map.entry("8.21", new TribunalInfo("tjrs", "RS")),
            Map.entry("8.22", new TribunalInfo("tjro", "RO")),
            Map.entry("8.23", new TribunalInfo("tjrr", "RR")),
            Map.entry("8.24", new TribunalInfo("tjsc", "SC")),
            Map.entry("8.25", new TribunalInfo("tjsp", "SP")),
            Map.entry("8.26", new TribunalInfo("tjse", "SE")),
            Map.entry("8.27", new TribunalInfo("tjto", "TO")),

            // === JUSTIÇA DO TRABALHO
            Map.entry("5.01", new TribunalInfo("trt1", "RJ")),
            Map.entry("5.02", new TribunalInfo("trt2", "SP")),
            Map.entry("5.03", new TribunalInfo("trt3", "MG")),
            Map.entry("5.04", new TribunalInfo("trt4", "RS")),
            Map.entry("5.05", new TribunalInfo("trt5", "BA")),
            Map.entry("5.06", new TribunalInfo("trt6", "PE")),
            Map.entry("5.07", new TribunalInfo("trt7", "CE")),
            Map.entry("5.08", new TribunalInfo("trt8", "PA")),
            Map.entry("5.09", new TribunalInfo("trt9", "PR")),
            Map.entry("5.10", new TribunalInfo("trt10", "DF")),
            Map.entry("5.11", new TribunalInfo("trt11", "AM")),
            Map.entry("5.12", new TribunalInfo("trt12", "SC")),
            Map.entry("5.13", new TribunalInfo("trt13", "PB")),
            Map.entry("5.14", new TribunalInfo("trt14", "RO")),
            Map.entry("5.15", new TribunalInfo("trt15", "SP")),
            Map.entry("5.16", new TribunalInfo("trt16", "MA")),
            Map.entry("5.17", new TribunalInfo("trt17", "ES")),
            Map.entry("5.18", new TribunalInfo("trt18", "GO")),
            Map.entry("5.19", new TribunalInfo("trt19", "AL")),
            Map.entry("5.20", new TribunalInfo("trt20", "SE")),
            Map.entry("5.21", new TribunalInfo("trt21", "RN")),
            Map.entry("5.22", new TribunalInfo("trt22", "PI")),
            Map.entry("5.23", new TribunalInfo("trt23", "MT")),
            Map.entry("5.24", new TribunalInfo("trt24", "MS")),

            // === JUSTIÇA ELEITORAL
            Map.entry("6.01", new TribunalInfo("tre-ac", "AC")),
            Map.entry("6.02", new TribunalInfo("tre-al", "AL")),
            Map.entry("6.03", new TribunalInfo("tre-am", "AM")),
            Map.entry("6.04", new TribunalInfo("tre-ap", "AP")),
            Map.entry("6.05", new TribunalInfo("tre-ba", "BA")),
            Map.entry("6.06", new TribunalInfo("tre-ce", "CE")),
            Map.entry("6.07", new TribunalInfo("tre-df", "DF")),
            Map.entry("6.08", new TribunalInfo("tre-es", "ES")),
            Map.entry("6.09", new TribunalInfo("tre-go", "GO")),
            Map.entry("6.10", new TribunalInfo("tre-ma", "MA")),
            Map.entry("6.11", new TribunalInfo("tre-mt", "MT")),
            Map.entry("6.12", new TribunalInfo("tre-ms", "MS")),
            Map.entry("6.13", new TribunalInfo("tre-mg", "MG")),
            Map.entry("6.14", new TribunalInfo("tre-pa", "PA")),
            Map.entry("6.15", new TribunalInfo("tre-pb", "PB")),
            Map.entry("6.16", new TribunalInfo("tre-pr", "PR")),
            Map.entry("6.17", new TribunalInfo("tre-pe", "PE")),
            Map.entry("6.18", new TribunalInfo("tre-pi", "PI")),
            Map.entry("6.19", new TribunalInfo("tre-rj", "RJ")),
            Map.entry("6.20", new TribunalInfo("tre-rn", "RN")),
            Map.entry("6.21", new TribunalInfo("tre-rs", "RS")),
            Map.entry("6.22", new TribunalInfo("tre-ro", "RO")),
            Map.entry("6.23", new TribunalInfo("tre-rr", "RR")),
            Map.entry("6.24", new TribunalInfo("tre-sc", "SC")),
            Map.entry("6.25", new TribunalInfo("tre-sp", "SP")),
            Map.entry("6.26", new TribunalInfo("tre-se", "SE")),
            Map.entry("6.27", new TribunalInfo("tre-to", "TO")),

            // === JUSTIÇA MILITAR ESTADUAL
            Map.entry("9.13", new TribunalInfo("tjmmg", "MG")),
            Map.entry("9.21", new TribunalInfo("tjmrs", "RS")),
            Map.entry("9.25", new TribunalInfo("tjmsp", "SP"))
    );

    public TribunalInfo resolver(String numeroProcesso) {
        if (numeroProcesso == null) {
            throw new BadRequestException("Número do processo não pode ser nulo");
        }

        // Sanitização: remove pontuações caso a requisição venha formatada
        String limpo = numeroProcesso.replaceAll("[^0-9]", "");

        if (!limpo.matches("\\d{20}")) {
            throw new BadRequestException("Número do processo deve conter exatamente 20 dígitos numéricos");
        }

        // NNNNNNN DD AAAA J TR OOOO -> posições 13 (J) e 14-15 (TR)
        String j = limpo.substring(13, 14);
        String tr = limpo.substring(14, 16);
        String chave = j + "." + tr;

        TribunalInfo info = TRIBUNAIS.get(chave);
        if (info == null) {
            throw new BadRequestException("Tribunal não mapeado para o segmento: " + chave);
        }

        return info;
    }
}