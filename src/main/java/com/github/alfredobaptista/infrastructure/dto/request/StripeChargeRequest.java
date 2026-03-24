package com.github.alfredobaptista.infrastructure.dto.request;

import java.math.BigDecimal;

public record StripeChargeRequest(
        BigDecimal amount,
        String currency,      // ← lowercase (ex: "usd", "aoa")
        String customer,      // ← ID do customer no Stripe (cus_xxx)
        String description,
        String metadata       // ← ID interno para rastreamento
) {}