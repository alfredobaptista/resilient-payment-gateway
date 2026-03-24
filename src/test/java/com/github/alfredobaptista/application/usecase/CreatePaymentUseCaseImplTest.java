package com.github.alfredobaptista.application.usecase;

import com.github.alfredobaptista.application.gateway.GetPaymentByIdempotencyKeyGateway;
import com.github.alfredobaptista.application.gateway.SavePaymentGateway;
import com.github.alfredobaptista.application.usecase.input.CreatePaymentInput;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.core.enums.PaymentStatus;
import com.github.alfredobaptista.core.exception.GatewayUnavailableException;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreatePaymentUseCaseImpl")
class CreatePaymentUseCaseImplTest {

    private final Clock clock = Clock.fixed(
            LocalDateTime.of(2026, 3, 23, 17, 0,0)
                    .atZone(ZoneId.of("UTC")).toInstant(),
            ZoneId.of("UTC")
    );

    @Mock
    private GetPaymentByIdempotencyKeyGateway getByIdempotencyKeyGateway;

    @Mock
    private SavePaymentGateway savePaymentGateway;

    private CreatePaymentUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreatePaymentUseCaseImpl(
                getByIdempotencyKeyGateway,
                savePaymentGateway,
                clock
        );
    }


    @Nested
    @DisplayName("When creating a payment successfully")
    class WhenCreatingPaymentSuccessfully {

        @Test
        @DisplayName("Should create payment and return the saved entity")
        void shouldCreatePaymentAndReturnSavedEntity() {
            // ARRANGE
            String idempotencyKey = UUID.randomUUID().toString();
            String customerId = UUID.randomUUID().toString();
            CreatePaymentInput input = PaymentInputFactory.buildInput(idempotencyKey, customerId);
            Payment savedPayment = PaymentFactory.buildPayment(idempotencyKey, customerId);

            when(getByIdempotencyKeyGateway.getPaymentByIdempotencyKey(idempotencyKey))
                    .thenReturn(Optional.empty());
            when(savePaymentGateway.save(any()))
                    .thenReturn(savedPayment);

            // ACT
            Payment result = useCase.createPayment(input);

            // ASSERT
            assertNotNull(result);
            assertNotNull(result.getId());
            assertEquals(input.amount(), result.getAmount());
            assertEquals(input.customerId(), result.getCustomerId());
            assertEquals(input.idempotencyKey(), result.getIdempotencyKey());
            assertEquals(PaymentStatus.PENDING, result.getStatus());

            verify(savePaymentGateway, times(1)).save(any());
        }

        @Test
        @DisplayName("Should check idempotency before saving")
        void shouldCheckIdempotencyBeforeSaving() {
            // ARRANGE
            String idempotencyKey = UUID.randomUUID().toString();
            String customerId = UUID.randomUUID().toString();
            CreatePaymentInput input = PaymentInputFactory.buildInput(idempotencyKey, customerId);

            when(getByIdempotencyKeyGateway.getPaymentByIdempotencyKey(idempotencyKey))
                    .thenReturn(Optional.empty());
            when(savePaymentGateway.save(any()))
                    .thenReturn(PaymentFactory.buildPayment(idempotencyKey, customerId));

            // ACT
            useCase.createPayment(input);

            // ASSERT — verify call order
            var inOrder = inOrder(getByIdempotencyKeyGateway, savePaymentGateway);
            inOrder.verify(getByIdempotencyKeyGateway).getPaymentByIdempotencyKey(idempotencyKey);
            inOrder.verify(savePaymentGateway).save(any());
        }
    }


    @Nested
    @DisplayName("When idempotency key has already been used")
    class WhenIdempotencyKeyHasAlreadyBeenUsed {

        @Test
        @DisplayName("Should return the existing payment without creating a new one")
        void shouldReturnExistingPaymentWithoutCreatingNewOne() {
            // ARRANGE
            String idempotencyKey = UUID.randomUUID().toString();
            Payment existingPayment = PaymentFactory.buildPayment(idempotencyKey, UUID.randomUUID().toString());
            CreatePaymentInput input = PaymentInputFactory.buildInput(idempotencyKey, UUID.randomUUID().toString());

            when(getByIdempotencyKeyGateway.getPaymentByIdempotencyKey(idempotencyKey))
                    .thenReturn(Optional.of(existingPayment));

            // ACT
            Payment result = useCase.createPayment(input);

            // ASSERT
            assertEquals(existingPayment, result);
            assertEquals(existingPayment.getId(), result.getId());
            verify(savePaymentGateway, never()).save(any());
        }

        @Test
        @DisplayName("Should return existing APPROVED payment")
        void shouldReturnExistingApprovedPayment() {
            // ARRANGE
            String idempotencyKey = UUID.randomUUID().toString();
            Payment approvedPayment = PaymentFactory.buildPayment(idempotencyKey, UUID.randomUUID().toString());
            approvedPayment.approve("tx_stripe_123", clock);

            when(getByIdempotencyKeyGateway.getPaymentByIdempotencyKey(idempotencyKey))
                    .thenReturn(Optional.of(approvedPayment));

            CreatePaymentInput input = PaymentInputFactory.buildInput(idempotencyKey, UUID.randomUUID().toString());

            // ACT
            Payment result = useCase.createPayment(input);

            // ASSERT
            assertEquals(PaymentStatus.APPROVED, result.getStatus());
            verify(savePaymentGateway, never()).save(any());
        }
    }


    @Nested
    @DisplayName("When infrastructure failure occurs")
    class WhenInfrastructureFailureOccurs {

        @Test
        @DisplayName("Should propagate GatewayUnavailableException when Redis is unavailable")
        void shouldPropagateExceptionWhenRedisIsUnavailable() {
            // ARRANGE
            String idempotencyKey = UUID.randomUUID().toString();
            CreatePaymentInput input = PaymentInputFactory.buildInput(idempotencyKey, UUID.randomUUID().toString());

            when(getByIdempotencyKeyGateway.getPaymentByIdempotencyKey(idempotencyKey))
                    .thenThrow(new GatewayUnavailableException("Redis unavailable"));

            // ACT + ASSERT
            assertThrows(GatewayUnavailableException.class,
                    () -> useCase.createPayment(input));

            verify(savePaymentGateway, never()).save(any());
        }

        @Test
        @DisplayName("Should propagate GatewayUnavailableException when database fails to save")
        void shouldPropagateExceptionWhenDatabaseIsUnavailable() {
            // ARRANGE
            String idempotencyKey = UUID.randomUUID().toString();
            CreatePaymentInput input = PaymentInputFactory.buildInput(idempotencyKey, UUID.randomUUID().toString());

            when(getByIdempotencyKeyGateway.getPaymentByIdempotencyKey(idempotencyKey))
                    .thenReturn(Optional.empty());

            when(savePaymentGateway.save(any()))
                    .thenThrow(new GatewayUnavailableException("Error persisting"));

            // ACT + ASSERT
            assertThrows(GatewayUnavailableException.class,
                    () -> useCase.createPayment(input));
        }
    }
}