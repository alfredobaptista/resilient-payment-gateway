package com.github.alfredobaptista.infrastructure.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PaymentRequest(

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal amount,

        @NotBlank(message = "Moeda é obrigatória")
        @Size(min = 3, max = 3, message = "Moeda deve ter exactamente 3 caracteres (ex: USD, AOA)")
        String currency,

        @NotBlank(message = "Cliente é obrigatório")
        String customerId,

        @Size(max = 255, message = "Descrição não pode ter mais de 255 caracteres")
        String description
) {}