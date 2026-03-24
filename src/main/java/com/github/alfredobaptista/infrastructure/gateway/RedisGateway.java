package com.github.alfredobaptista.infrastructure.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alfredobaptista.core.exception.GatewayUnavailableException;
import com.github.alfredobaptista.infrastructure.model.PaymentModel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisGateway  {

    private static final Logger log = LoggerFactory.getLogger(RedisGateway.class);
    private static final String KEY_PREFIX = "idempotency:payment:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${payment.idempotency.ttl-hours:24}")
    private long ttlHours;

    public Optional<PaymentModel> findByKey(String idempotencyKey) {
        String redisKey = KEY_PREFIX + idempotencyKey;
        try {
            String json = redisTemplate.opsForValue().get(redisKey);
            if (json == null) return Optional.empty();

            PaymentModel payment = objectMapper.readValue(json, PaymentModel.class);
            log.debug("Idempotency hit — Key: {}", idempotencyKey);
            return Optional.of(payment);
        } catch (Exception e) {
            // Redis indisponível — lança exceção para não processar pagamento duplicado
            log.error("Erro ao consultar idempotência no Redis — Key: {}", idempotencyKey, e);
            throw new GatewayUnavailableException(
                    "Erro ao consultar chave de idempotência: " + idempotencyKey
            );
        }
    }

    public void save(PaymentModel payment) {
        String redisKey = KEY_PREFIX + payment.getIdempotencyKey();
        try {
            String json = objectMapper.writeValueAsString(payment);
            redisTemplate.opsForValue().set(redisKey, json, Duration.ofHours(ttlHours));
            log.debug("Idempotency guardado — Key: {}, TTL: {}h", payment.getIdempotencyKey(), ttlHours);
        } catch (Exception e) {
            // Redis indisponível — lança exceção para não quebrar idempotência silenciosamente
            log.error("Erro ao guardar idempotência no Redis — Key: {}", payment.getIdempotencyKey(), e);
            throw new GatewayUnavailableException(
                    "Erro ao guardar chave de idempotência: " + payment.getIdempotencyKey()
            );
        }
    }
}