package com.github.alfredobaptista.infrastructure.gateway;

import com.github.alfredobaptista.application.gateway.GetPaymentByIdempotencyKeyGateway;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.core.exception.GatewayUnavailableException;
import com.github.alfredobaptista.infrastructure.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class GetPaymentIdempotencyByKeyGatewayImpl implements GetPaymentByIdempotencyKeyGateway {

    private final RedisGateway redisIdempotencyPort;
    private final PaymentMapper mapper;

    @Override
    public Optional<Payment> getPaymentByIdempotencyKey(String key) {
        try {
            return redisIdempotencyPort.findByKey(key)
                    .map(mapper::toEntity);
        } catch (Exception e) {
            throw new GatewayUnavailableException(
                    "Erro ao buscar chave de idempotência: " + key
            );
        }
    }
}
