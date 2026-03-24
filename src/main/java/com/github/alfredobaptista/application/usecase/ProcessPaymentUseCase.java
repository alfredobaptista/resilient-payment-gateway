package com.github.alfredobaptista.application.usecase;

import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.application.usecase.input.CreatePaymentInput;

public interface ProcessPaymentUseCase {
    Payment execute(CreatePaymentInput input);
}