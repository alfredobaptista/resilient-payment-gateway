package com.github.alfredobaptista.application.usecase;

import com.github.alfredobaptista.application.gateway.GetPaymentByIdempotencyKeyGateway;
import com.github.alfredobaptista.application.gateway.SavePaymentGateway;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.application.usecase.input.CreatePaymentInput;

import java.time.Clock;
import java.util.Optional;

public class CreatePaymentUseCaseImpl implements CreatePaymentUseCase {

    private final GetPaymentByIdempotencyKeyGateway getByIdempotencyKey;
    private final SavePaymentGateway savePaymentGateway;
    private final Clock clock;

    public CreatePaymentUseCaseImpl(
            GetPaymentByIdempotencyKeyGateway getByIdempotencyKey,
            SavePaymentGateway savePaymentGateway,
            Clock clock
    ) {
        this.getByIdempotencyKey = getByIdempotencyKey;
        this.savePaymentGateway = savePaymentGateway;
        this.clock = clock;
    }

    @Override
    public Payment createPayment(CreatePaymentInput input) {
        // Idempotência — retorna pagamento existente se chave já foi usada
        Optional<Payment> existing = getByIdempotencyKey
                .getPaymentByIdempotencyKey(input.idempotencyKey());
        if (existing.isPresent()) {
            return existing.get();
        }

        // Use case controla a construção — estado inicial PENDING garantido
        Payment payment = new Payment(
                input.idempotencyKey(),
                input.amount(),
                input.currency(),
                input.customerId(),
                input.description(),
                clock
        );
        return savePaymentGateway.save(payment);
    }
}