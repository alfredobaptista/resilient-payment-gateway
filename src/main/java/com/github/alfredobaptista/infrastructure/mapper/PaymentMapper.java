package com.github.alfredobaptista.infrastructure.mapper;

import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.infrastructure.dto.response.PaymentResponse;
import com.github.alfredobaptista.infrastructure.model.PaymentModel;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentModel toModel(Payment payment) {
        return PaymentModel.builder()
                .id(payment.getId())
                .idempotencyKey(payment.getIdempotencyKey())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .customerId(payment.getCustomerId())
                .description(payment.getDescription())
                .status(payment.getStatus())
                .gatewayTransactionId(payment.getGatewayTransactionId())
                .failureReason(payment.getFailureReason())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    public Payment toEntity(PaymentModel model) {
        return new Payment(
                model.getId(),                               // ← corrigido
                model.getIdempotencyKey(),
                model.getAmount(),
                model.getCurrency(),
                model.getCustomerId(),
                model.getDescription(),
                model.getStatus(),                           // ← corrigido
                model.getGatewayTransactionId(),
                model.getFailureReason(),
                model.getCreatedAt(),
                model.getUpdatedAt()
        );
    }

    // -------------------------------------------------------------------------
    // Payment → PaymentResponse (saída da API)
    // -------------------------------------------------------------------------
    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId() != null ? payment.getId().toString() : null,
                payment.getAmount(),
                payment.getCurrency(),
                payment.getCustomerId(),
                payment.getStatus().getDescription(),
                payment.getGatewayTransactionId(),
                payment.getFailureReason(),
                payment.getCreatedAt()
        );
    }
}
