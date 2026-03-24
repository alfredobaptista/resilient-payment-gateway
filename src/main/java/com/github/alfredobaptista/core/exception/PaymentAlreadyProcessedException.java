package com.github.alfredobaptista.core.exception;

import com.github.alfredobaptista.core.enums.PaymentStatus;

public class PaymentAlreadyProcessedException extends RuntimeException {

    private final PaymentStatus currentStatus;

    public PaymentAlreadyProcessedException(PaymentStatus currentStatus) {
        super("Pagamento já processado. Status actual: " + currentStatus.getDescription());
        this.currentStatus = currentStatus;
    }

    public PaymentStatus getCurrentStatus() {
        return currentStatus;
    }
}