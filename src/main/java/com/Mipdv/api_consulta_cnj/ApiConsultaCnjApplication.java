package com.Mipdv.api_consulta_cnj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiConsultaCnjApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiConsultaCnjApplication.class, args);
	}

}
