package com.github.alfredobaptista.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Resilient Payment Gateway API")
                        .version("v1")
                        .description("API para processamento resiliente de pagamentos, com idempotência e circuit breakers.")
                        .contact(new Contact()
                                .name("Alfredo Baptista")
                                .email("baptistaalfredo81@gmail.com")
                                .url("https://github.com/alfredobaptista"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}