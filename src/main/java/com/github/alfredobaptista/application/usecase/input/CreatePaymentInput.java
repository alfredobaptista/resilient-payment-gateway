package com.github.alfredobaptista.application.usecase.input;

import java.math.BigDecimal;

public record CreatePaymentInput(
        String idempotencyKey,
        BigDecimal amount,
        String currency,
        String customerId,
        String description
) {}