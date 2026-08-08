package com.Mipdv.api_consulta_cnj.infrastructure.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DataUtil {
    
    private static final DateTimeFormatter FORMATO_CNJ =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    private static final DateTimeFormatter FORMATO_BRASIL =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    private static final ZoneId FUSO_BRASIL = ZoneId.of
            ("America/Sao_Paulo");
    
    
    public static String formartarDataBruta(String dataBruta){
        if(dataBruta == null || !dataBruta.matches("\\d{14}")){
            return dataBruta;
        }
        LocalDateTime dt = LocalDateTime.parse(dataBruta, FORMATO_CNJ);
        return dt.format(FORMATO_BRASIL);
    }
    public static String formatarDataIso(String dataIso) {
        if (dataIso == null || dataIso.isBlank()) {
            return dataIso;
        }
        try {
            Instant instant = Instant.parse(dataIso);
            return instant.atZone(FUSO_BRASIL).format(FORMATO_BRASIL);
        } catch (Exception e) {
            return dataIso;
        }
    }
        
    }
