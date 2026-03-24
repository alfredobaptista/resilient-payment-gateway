package com.github.alfredobaptista.factory;

import com.github.alfredobaptista.application.usecase.input.CreatePaymentInput;

import java.math.BigDecimal;

public class PaymentInputFactory {

    public static  CreatePaymentInput buildInput(String idempotencyKey, String customerId) {
        return new CreatePaymentInput(
                idempotencyKey,
                new BigDecimal("2000.00"),
                "AOA",
                customerId,
                "Test payment"
        );
    }
}
