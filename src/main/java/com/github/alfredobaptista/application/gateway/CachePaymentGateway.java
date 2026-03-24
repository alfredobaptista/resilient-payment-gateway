package com.github.alfredobaptista.application.gateway;

import com.github.alfredobaptista.core.entity.Payment;

public interface CachePaymentGateway {
    void save(Payment payment);
}
