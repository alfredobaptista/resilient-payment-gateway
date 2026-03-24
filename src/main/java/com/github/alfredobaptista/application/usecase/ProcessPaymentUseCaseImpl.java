package com.github.alfredobaptista.application.usecase;

import com.github.alfredobaptista.application.gateway.CachePaymentGateway;
import com.github.alfredobaptista.application.gateway.GetPaymentByIdempotencyKeyGateway;
import com.github.alfredobaptista.application.gateway.PaymentGatewayPort;
import com.github.alfredobaptista.application.gateway.SavePaymentGateway;
import com.github.alfredobaptista.application.usecase.input.CreatePaymentInput;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.core.exception.GatewayUnavailableException;

import java.time.Clock;

public class ProcessPaymentUseCaseImpl implements ProcessPaymentUseCase {

    private final GetPaymentByIdempotencyKeyGateway getByIdempotencyKey;
    private final CachePaymentGateway cachePaymentGateway;
    private final SavePaymentGateway savePaymentGateway;
    private final PaymentGatewayPort paymentGatewayPort;
    private final Clock clock;

    public ProcessPaymentUseCaseImpl(
            GetPaymentByIdempotencyKeyGateway getByIdempotencyKey,
            CachePaymentGateway cachePaymentGateway,
            SavePaymentGateway savePaymentGateway,
            PaymentGatewayPort paymentGatewayPort,
            Clock clock
    ) {
        this.getByIdempotencyKey = getByIdempotencyKey;
        this.cachePaymentGateway = cachePaymentGateway;
        this.savePaymentGateway = savePaymentGateway;
        this.paymentGatewayPort = paymentGatewayPort;
        this.clock = clock;
    }

    @Override
    public Payment execute(CreatePaymentInput input) {
        return getByIdempotencyKey
                .getPaymentByIdempotencyKey(input.idempotencyKey())
                .orElseGet(() -> process(input));
    }

    private Payment process(CreatePaymentInput input) {
        // Constrói e persiste o pagamento com status PENDING
        Payment payment = savePaymentGateway.save(new Payment(
                input.idempotencyKey(),
                input.amount(),
                input.currency(),
                input.customerId(),
                input.description(),
                clock
        ));

        try {
            // Envia ao gateway externo
            String transactionId = paymentGatewayPort.charge(payment);
            payment.approve(transactionId, clock);
        } catch (GatewayUnavailableException e) {
            // Gateway indisponível — salva como FAILED e retorna sem relançar
            payment.fail(e.getMessage(), clock);
        }
        // Persiste o estado final (APPROVED ou FAILED)
        Payment savedPayment = savePaymentGateway.save(payment);
        cachePaymentGateway.save(savedPayment);
        return savedPayment;
    }
}