package com.Mipdv.api_consulta_cnj.infrastructure.Client.configs;

import com.Mipdv.api_consulta_cnj.infrastructure.exceptions.BadRequestException;
import com.Mipdv.api_consulta_cnj.infrastructure.exceptions.ConflictException;
import com.Mipdv.api_consulta_cnj.infrastructure.exceptions.UnauthorizedException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FeignError implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        String mensagemErro = mensagemErro(response);

        return switch (response.status()) {
            case 400 -> new BadRequestException("Erro de requisição: " + mensagemErro);
            //usar -> em vez de return (java 14+)
            case 401, 403 -> new UnauthorizedException("Token inválido ou não autorizado: " + mensagemErro);
            case 409 -> new ConflictException("Atributo já existente: " + mensagemErro);
            default -> new ErrorDecoder.Default().decode(s, response);
        };
    }

    private String mensagemErro(Response response) {
        try {
            if (Objects.isNull(response.body())) {
                return "";
            }
            return new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
