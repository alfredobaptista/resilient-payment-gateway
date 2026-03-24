package com.github.alfredobaptista.infrastructure.gateway;

import com.github.alfredobaptista.application.gateway.CachePaymentGateway;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.core.exception.GatewayUnavailableException;
import com.github.alfredobaptista.infrastructure.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CachePayementGateway implements CachePaymentGateway {
    private final RedisGateway redisGateway;
    private final PaymentMapper mapper;

    @Override
    public void save(Payment payment) {
        try {
            redisGateway.save(mapper.toModel(payment));
        } catch (Exception e) {
            throw new GatewayUnavailableException("Erro ao salvar payment: " + payment);
        }
    }
}
