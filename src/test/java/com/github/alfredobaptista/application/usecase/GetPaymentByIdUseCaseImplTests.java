package com.github.alfredobaptista.application.usecase;

import com.github.alfredobaptista.application.gateway.GetPaymentByIdGateway;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.core.exception.NotFoundException;
import com.github.alfredobaptista.factory.PaymentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetPaymentByIdUseCaseImplTests {

    @Mock
    private GetPaymentByIdGateway getPaymentByIdGateway;

    private GetPaymentByIdUseCase getPaymentByIdUseCase;

    @BeforeEach
    void setUp() {
        getPaymentByIdUseCase = new GetPaymentByIdUseCaseImpl(getPaymentByIdGateway);
    }

    @Nested
    @DisplayName("when get payment by id sucessfully")
    class whenGetPaymentSucessfully{
        @Test
        @DisplayName("should get payment by id and return it")
        void sholudGetPaymentByIdAndReturnPayment(){
            //ARRANGE
            String idempotencyKey = UUID.randomUUID().toString();
            String customerId = UUID.randomUUID().toString();
            Payment savedPayment = PaymentFactory.buildPayment(idempotencyKey, customerId);
            when(getPaymentByIdGateway.getPaymentById(savedPayment.getId())).thenReturn(Optional.of(savedPayment));

            //ACT
            Payment payment = getPaymentByIdUseCase.getPaymentById(savedPayment.getId());
            //ASSERT
            assertNotNull(payment);
            assertEquals(savedPayment, payment);
        }
    }

    @Nested
    @DisplayName("when get payment by id return not found")
    class WhenGetPaymentByIdReturnNotFound {

        @Test
        @DisplayName("when get payment by id should throw exception")
        void shouldReturnNotFoundException(){
            //ARRANGE
            String idempotencyKey = UUID.randomUUID().toString();
            String customerId = UUID.randomUUID().toString();
            Payment savedPayment = PaymentFactory.buildPayment(idempotencyKey, customerId);
            when(getPaymentByIdGateway.getPaymentById(savedPayment.getId())).thenReturn(Optional.empty());

            //ACT + //ASSERT
            assertThrows(NotFoundException.class, ()-> getPaymentByIdUseCase.getPaymentById(savedPayment.getId()));
        }

    }
}
