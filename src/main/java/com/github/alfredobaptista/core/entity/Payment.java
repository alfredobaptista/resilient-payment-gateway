package com.github.alfredobaptista.core.entity;

import com.github.alfredobaptista.core.enums.PaymentStatus;
import com.github.alfredobaptista.core.exception.PaymentAlreadyProcessedException;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
    private UUID id;
    private String idempotencyKey;
    private BigDecimal amount;
    private String currency;
    private String customerId;
    private String description;
    private PaymentStatus status;
    private String gatewayTransactionId;
    private String failureReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Payment() {}

    public Payment(
            String idempotencyKey,
            BigDecimal amount,
            String currency,
            String customerId,
            String description,
            Clock clock
    ) {
        this.id = UUID.randomUUID();
        this.idempotencyKey = idempotencyKey;
        this.amount = amount;
        this.currency = currency;
        this.customerId = customerId;
        this.description = description;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now(clock);
        this.updatedAt = LocalDateTime.now(clock);
    }

    public Payment(
            UUID id,
            String idempotencyKey,
            BigDecimal amount,
            String currency,
            String customerId,
            String description,
            PaymentStatus status,
            String gatewayTransactionId,
            String failureReason,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.idempotencyKey = idempotencyKey;
        this.amount = amount;
        this.currency = currency;
        this.customerId = customerId;
        this.description = description;
        this.status = status;
        this.gatewayTransactionId = gatewayTransactionId;
        this.failureReason = failureReason;
        this.createdAt = createdAt;   // ← vem do banco, não sobrescreve
        this.updatedAt = updatedAt;
    }

    // -------------------------------------------------------------------------
    // Regras de negócio
    // -------------------------------------------------------------------------

    public void approve(String gatewayTransactionId, Clock clock) {
        validateNotAlreadyProcessed();
        this.status = PaymentStatus.APPROVED;
        this.gatewayTransactionId = gatewayTransactionId;
        this.updatedAt = LocalDateTime.now(clock);
    }

    public void fail(String reason, Clock clock) {
        validateNotAlreadyProcessed();
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = LocalDateTime.now(clock);
    }


    public void pend(String reason, Clock clock) {
        validateNotAlreadyProcessed();
        this.status = PaymentStatus.PENDING;
        this.failureReason = reason;
        this.updatedAt = LocalDateTime.now(clock);
    }

    public boolean isAlreadyProcessed() {
        return status == PaymentStatus.APPROVED || status == PaymentStatus.FAILED;
    }

    private void validateNotAlreadyProcessed() {
        if (isAlreadyProcessed()) {
            throw new PaymentAlreadyProcessedException(this.status);
        }
    }


    public UUID getId() { return id; }

    public String getIdempotencyKey() { return idempotencyKey; }

    public BigDecimal getAmount() { return amount; }

    public String getCurrency() { return currency; }

    public String getCustomerId() { return customerId; }

    public String getDescription() { return description; }

    public PaymentStatus getStatus() { return status; }

    public String getGatewayTransactionId() { return gatewayTransactionId; }

    public String getFailureReason() { return failureReason; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
}