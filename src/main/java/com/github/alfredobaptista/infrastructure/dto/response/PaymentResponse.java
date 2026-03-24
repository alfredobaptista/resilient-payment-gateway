package com.github.alfredobaptista.infrastructure.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        String id,
        BigDecimal amount,
        String currency,        //
        String customerId,
        String status,
        String gatewayTransactionId,
        String failureReason,
        LocalDateTime createdAt
) {}