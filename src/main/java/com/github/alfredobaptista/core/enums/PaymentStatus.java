package com.github.alfredobaptista.core.enums;

public enum PaymentStatus {
    PENDING("pendente"),
    APPROVED("aprovado"),
    FAILED("falhou");
    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
