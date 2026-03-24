package com.github.alfredobaptista.infrastructure.gateway; // ← pacote corrigido

import com.github.alfredobaptista.application.gateway.PaymentGatewayPort;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.core.exception.GatewayUnavailableException;
import com.github.alfredobaptista.infrastructure.dto.request.StripeChargeRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ADAPTER — implementa o port PaymentGatewayPort usando Stripe + FeignClient.
 *
 * Estratégia de resiliência:
 * - @Retry: tenta novamente em falhas transitórias (ex: timeout)
 * - @CircuitBreaker: abre o circuito após 50% de falhas, protege contra cascata
 *
 * Ordem correta: CircuitBreaker (externo) → Retry (interno) → chamada real
 */
@RequiredArgsConstructor
@Component
public class StripeGateway implements PaymentGatewayPort {

    private static final Logger log = LoggerFactory.getLogger(StripeGateway.class);

    private final StripeClient stripeClient;

    @Override
    @CircuitBreaker(name = "stripe", fallbackMethod = "fallback")
    @Retry(name = "stripe")
    public String charge(Payment payment) {
        log.info("Enviando pagamento ao Stripe — ID: {}, Valor: {}",
                payment.getId(), payment.getAmount());

        StripeChargeRequest request = new StripeChargeRequest(
                payment.getAmount(),
                payment.getCurrency().toLowerCase(),
                payment.getCustomerId(),
                payment.getDescription(),
                payment.getId().toString()
        );

        var response = stripeClient.charge(request);

        log.info("Stripe respondeu com sucesso — TxId: {}", response.id());
        return response.id();
    }

    /**
     * FALLBACK — chamado quando Circuit Breaker está OPEN ou Retry esgotado.
     * Assinatura deve ser idêntica ao método original + Exception como último parâmetro.
     */
    public String fallback(Payment payment, Exception ex) {
        log.warn("Circuit Breaker activo — fallback para pagamento ID: {}. Motivo: {}",
                payment.getId(), ex.getMessage());
        throw new GatewayUnavailableException(
                "Gateway indisponível após retentativas"
        );
    }
}