package com.github.alfredobaptista.application.gateway;

import com.github.alfredobaptista.core.entity.Payment;
import java.util.Optional;

public interface GetPaymentByIdempotencyKeyGateway {
    Optional<Payment> getPaymentByIdempotencyKey(String key);
}