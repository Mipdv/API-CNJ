package com.Mipdv.api_consulta_cnj.infrastructure.Client.configs;


import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Value("${cnj.api.token}")
    private String apiKey;

    @Bean
    public RequestInterceptor requestInterceptor(){
        //Colocar um try/catch com UnauthorizedException
        return requestTemplate -> {
            requestTemplate.header(
                    "Authorization", "APIKey " + apiKey
            );
            requestTemplate.header(
                    "Content-Type",
                    "application/json"
            );
        };
    }

    @Bean
    public FeignError feignError(){
        return new FeignError();
    }

}
