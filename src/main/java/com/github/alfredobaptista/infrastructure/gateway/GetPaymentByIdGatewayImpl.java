package com.github.alfredobaptista.infrastructure.gateway;

import com.github.alfredobaptista.application.gateway.GetPaymentByIdGateway;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.core.exception.GatewayUnavailableException;
import com.github.alfredobaptista.infrastructure.mapper.PaymentMapper;
import com.github.alfredobaptista.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class GetPaymentByIdGatewayImpl implements GetPaymentByIdGateway {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public Optional<Payment> getPaymentById(UUID paymentId) {
        try {
            return paymentRepository.findById(paymentId)
                    .map(paymentMapper::toEntity);
        } catch (DataAccessException e) {
            throw new GatewayUnavailableException(
                    "Erro ao buscar pagamento com id: " + paymentId
            );
        }
    }

}