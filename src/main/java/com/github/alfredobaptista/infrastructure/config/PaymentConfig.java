package com.github.alfredobaptista.infrastructure.config;

import com.github.alfredobaptista.application.gateway.*;
import com.github.alfredobaptista.application.usecase.CreatePaymentUseCase;
import com.github.alfredobaptista.application.usecase.CreatePaymentUseCaseImpl;
import com.github.alfredobaptista.application.usecase.GetPaymentByIdUseCase;
import com.github.alfredobaptista.application.usecase.GetPaymentByIdUseCaseImpl;
import com.github.alfredobaptista.application.usecase.ProcessPaymentUseCase;
import com.github.alfredobaptista.application.usecase.ProcessPaymentUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class PaymentConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public CreatePaymentUseCase createPaymentUseCase(
            GetPaymentByIdempotencyKeyGateway getByIdempotencyKey,
            SavePaymentGateway savePaymentGateway,
            Clock clock
    ) {
        return new CreatePaymentUseCaseImpl(
                getByIdempotencyKey,
                savePaymentGateway,
                clock
        );
    }

    @Bean
    public GetPaymentByIdUseCase getPaymentByIdUseCase(
            GetPaymentByIdGateway getPaymentByIdGateway
    ) {
        return new GetPaymentByIdUseCaseImpl(getPaymentByIdGateway);
    }

    @Bean
    public ProcessPaymentUseCase processPaymentUseCase(
            GetPaymentByIdempotencyKeyGateway getByIdempotencyKey,
            CachePaymentGateway cachePaymentGateway,
            SavePaymentGateway savePaymentGateway,
            PaymentGatewayPort paymentGatewayPort,
            Clock clock
    ) {
        return new ProcessPaymentUseCaseImpl(
                getByIdempotencyKey,
                cachePaymentGateway,
                savePaymentGateway,
                paymentGatewayPort,
                clock
        );
    }
}