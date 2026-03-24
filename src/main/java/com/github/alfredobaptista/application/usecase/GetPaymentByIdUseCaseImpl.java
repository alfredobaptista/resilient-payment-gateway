package com.github.alfredobaptista.application.usecase;

import com.github.alfredobaptista.application.gateway.GetPaymentByIdGateway;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.core.exception.NotFoundException;

import java.util.UUID;

public class GetPaymentByIdUseCaseImpl implements GetPaymentByIdUseCase {

    private final GetPaymentByIdGateway getPaymentByIdGateway;

    public GetPaymentByIdUseCaseImpl(GetPaymentByIdGateway getPaymentByIdGateway) {
        this.getPaymentByIdGateway = getPaymentByIdGateway;
    }

    @Override
    public Payment getPaymentById(UUID paymentId) {
        return getPaymentByIdGateway.getPaymentById(paymentId)
                .orElseThrow(() -> new NotFoundException(
                        "Pagamento não encontrado"));
    }
}