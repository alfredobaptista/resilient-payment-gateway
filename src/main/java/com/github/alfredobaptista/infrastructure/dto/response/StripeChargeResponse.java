package com.github.alfredobaptista.infrastructure.dto.response;

public record StripeChargeResponse(
        String id,      // ID do pagamento
        String status,    // "succeeded", "pending", "failed"
        Long amount,      // valor em centavos
        String currency  // "usd", "eur", etc
) {
}
