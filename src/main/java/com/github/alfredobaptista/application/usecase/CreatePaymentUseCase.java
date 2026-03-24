package com.github.alfredobaptista.application.usecase;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.application.usecase.input.CreatePaymentInput;

public interface CreatePaymentUseCase {
    Payment createPayment(CreatePaymentInput input);
}