package com.github.alfredobaptista.application.gateway;

import com.github.alfredobaptista.core.entity.Payment;

/**
 * @throws com.github.alfredobaptista.core.exception.GatewayUnavailableException
 *         se o gateway externo estiver indisponível
 */
public interface PaymentGatewayPort {
    String charge(Payment payment);
}