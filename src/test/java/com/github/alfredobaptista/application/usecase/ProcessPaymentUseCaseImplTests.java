package com.github.alfredobaptista.application.usecase;

import com.github.alfredobaptista.application.gateway.CachePaymentGateway;
import com.github.alfredobaptista.application.gateway.GetPaymentByIdempotencyKeyGateway;
import com.github.alfredobaptista.application.gateway.PaymentGatewayPort;
import com.github.alfredobaptista.application.gateway.SavePaymentGateway;
import com.github.alfredobaptista.application.usecase.input.CreatePaymentInput;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.core.enums.PaymentStatus;
import com.github.alfredobaptista.factory.PaymentFactory;
import com.github.alfredobaptista.factory.PaymentInputFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessPaymentUseCaseImplTests {

    @Mock
    private GetPaymentByIdempotencyKeyGateway getPaymentByIdempotencyKeyGateway;

    @Mock
    private SavePaymentGateway savePaymentGateway;

    @Mock
    private PaymentGatewayPort paymentGatewayPort;

    @Mock
    private CachePaymentGateway cachePaymentGateway;


    private final Clock clock = PaymentFactory.clock;

    private ProcessPaymentUseCase processPaymentUseCase;

    @BeforeEach
    void setUp() {
        processPaymentUseCase = new ProcessPaymentUseCaseImpl(
                getPaymentByIdempotencyKeyGateway,
                cachePaymentGateway,
                savePaymentGateway,
                paymentGatewayPort,
                clock
        );
    }

    @Nested
    @DisplayName("when payment is process sucessfully")
    class WhenProcessPaymentUseCaseSucessfully {

        @Test
        @DisplayName("Should process payment successfully")
        void shouldProcessPaymentSuccessfully() {
            // ARRANGE
            String idempotencyKey = UUID.randomUUID().toString();
            String customerId = UUID.randomUUID().toString();
            String transactionId = "txn_" + UUID.randomUUID().toString();

            CreatePaymentInput input = PaymentInputFactory.buildInput(idempotencyKey, customerId);

            // Payment inicial (antes de aprovar)
            Payment pendingPayment = PaymentFactory.buildPayment(idempotencyKey, customerId);

            when(getPaymentByIdempotencyKeyGateway.getPaymentByIdempotencyKey(idempotencyKey))
                    .thenReturn(Optional.empty());

            when(savePaymentGateway.save(any(Payment.class)))
                    .thenReturn(pendingPayment)                     // 1ª chamada
                    .thenAnswer(invocation -> invocation.getArgument(0)
                    );  // 2ª chamada e posteriores

            when(paymentGatewayPort.charge(any(Payment.class)))
                    .thenReturn(transactionId);

            doNothing().when(cachePaymentGateway).save(any(Payment.class));

            // ACT
            Payment result = processPaymentUseCase.execute(input);

            // ASSERT
            assertNotNull(result);
            assertEquals(PaymentStatus.APPROVED, result.getStatus());
            assertEquals(transactionId, result.getGatewayTransactionId());

            // Verificações
            verify(savePaymentGateway, times(2)).save(any(Payment.class));
            verify(cachePaymentGateway, times(1)).save(any(Payment.class));


            verify(savePaymentGateway, times(1)).save(argThat(p ->
                    p.getStatus() == PaymentStatus.PENDING &&
                            p.getGatewayTransactionId() == null
            ));

            verify(savePaymentGateway, times(1)).save(argThat(p ->
                    p.getStatus() == PaymentStatus.APPROVED &&
                            transactionId.equals(p.getGatewayTransactionId())
            ));
        }
    }


    @Nested
    @DisplayName("when payment already processed")
    class WhenPaymentAlreadyProcess {

        @Test
        void shouldReturnCachedPayment() {
            // ARRANGE
            String idempotencyKey = UUID.randomUUID().toString();
            String customerId = UUID.randomUUID().toString();
            String transactionId = "txn_" + UUID.randomUUID().toString();

            CreatePaymentInput input = PaymentInputFactory.buildInput(idempotencyKey, customerId);

            // Payment inicial (antes de aprovar)
            Payment pendingPayment = PaymentFactory.buildPayment(idempotencyKey, customerId);
            pendingPayment.approve(transactionId, clock);

            when(getPaymentByIdempotencyKeyGateway.getPaymentByIdempotencyKey(idempotencyKey))
                    .thenReturn(Optional.of(pendingPayment));

            // ACT
            Payment result = processPaymentUseCase.execute(input);

            // ASSERT
            assertNotNull(result);
            assertEquals(PaymentStatus.APPROVED, result.getStatus());
            assertEquals(transactionId, result.getGatewayTransactionId());

            // Verificações
            verify(savePaymentGateway, times(0)).save(any(Payment.class));
            verify(cachePaymentGateway, times(0)).save(any(Payment.class));
            verify(paymentGatewayPort, times(0)).charge(any(Payment.class));
        }
    }

}
