package com.github.alfredobaptista.application.gateway;

import com.github.alfredobaptista.core.entity.Payment;

public interface SavePaymentGateway {
    Payment save(Payment payment);
}