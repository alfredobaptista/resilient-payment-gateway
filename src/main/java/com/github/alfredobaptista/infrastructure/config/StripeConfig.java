package com.github.alfredobaptista.infrastructure.config;

import feign.RequestInterceptor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class StripeConfig {

    private final String apiKey;

    // Injeção via construtor — mais seguro e testável
    public StripeConfig(@Value("${gateway.stripe.api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    // Valida na inicialização — falha rápido se a chave não estiver configurada
    @PostConstruct
    public void validate() {
        if (!StringUtils.hasText(apiKey)) {
            throw new IllegalStateException(
                    "Propriedade 'gateway.stripe.api-key' não configurada. " +
                            "Verifique o application.yaml."
            );
        }
    }

    @Bean
    public RequestInterceptor stripeAuthInterceptor() {
        return template -> template.header("Authorization", "Bearer " + apiKey);
    }
}