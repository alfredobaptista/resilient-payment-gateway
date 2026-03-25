package com.github.alfredobaptista.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
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