package com.github.alfredobaptista.factory;

import com.github.alfredobaptista.core.entity.Payment;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PaymentFactory {

    public static final Clock clock = Clock.fixed(
            LocalDateTime.of(2026, 3, 23, 17, 0,0)
            .atZone(ZoneId.of("UTC")).toInstant(),
            ZoneId.of("UTC")
    );

    public static Payment buildPayment(String idempotencyKey, String customerId) {
        return new Payment(
                idempotencyKey,
                new BigDecimal("2000.00"),
                "AOA",
                customerId,
                "Test payment",
                clock
        );
    }
}
