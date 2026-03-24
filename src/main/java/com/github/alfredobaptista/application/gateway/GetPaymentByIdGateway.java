package com.github.alfredobaptista.application.gateway;

import com.github.alfredobaptista.core.entity.Payment;
import java.util.Optional;
import java.util.UUID;

public interface GetPaymentByIdGateway {
    Optional<Payment> getPaymentById(UUID paymentId);
}