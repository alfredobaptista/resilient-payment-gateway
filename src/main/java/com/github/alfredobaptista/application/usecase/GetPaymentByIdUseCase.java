package com.github.alfredobaptista.application.usecase;

import com.github.alfredobaptista.core.entity.Payment;

import java.util.UUID;

/**
 * @throws com.github.alfredobaptista.core.exception.NotFoundException
 *         se nenhum pagamento for encontrado com o ID fornecido
 */
public interface GetPaymentByIdUseCase {
    Payment getPaymentById(UUID paymentId);
}