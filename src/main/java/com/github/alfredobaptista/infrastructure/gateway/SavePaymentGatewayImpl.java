package com.github.alfredobaptista.infrastructure.gateway; // ← pacote corrigido

import com.github.alfredobaptista.application.gateway.SavePaymentGateway;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.core.exception.GatewayUnavailableException;
import com.github.alfredobaptista.infrastructure.mapper.PaymentMapper;
import com.github.alfredobaptista.infrastructure.model.PaymentModel;
import com.github.alfredobaptista.infrastructure.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SavePaymentGatewayImpl implements SavePaymentGateway {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public Payment save(Payment payment) {
        try {
            PaymentModel model = paymentMapper.toModel(payment);
            PaymentModel saved = paymentRepository.save(model);
            return paymentMapper.toEntity(saved);
        } catch (DataAccessException e) {
            // Traduz exceção de infraestrutura para o domínio
            // evitando que Spring vaze para a camada de application
            System.out.println(e.getMessage());
            throw new GatewayUnavailableException(
                    "Erro ao persistir pagamento com id: " + payment.getId()
            );
        }
    }
}