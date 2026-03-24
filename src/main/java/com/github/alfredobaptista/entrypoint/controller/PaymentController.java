package com.github.alfredobaptista.entrypoint.controller;
import com.github.alfredobaptista.application.usecase.GetPaymentByIdUseCase;
import com.github.alfredobaptista.application.usecase.ProcessPaymentUseCase;
import com.github.alfredobaptista.application.usecase.input.CreatePaymentInput;
import com.github.alfredobaptista.core.entity.Payment;
import com.github.alfredobaptista.infrastructure.dto.request.PaymentRequest;
import com.github.alfredobaptista.infrastructure.dto.response.ApiResponse;
import com.github.alfredobaptista.infrastructure.dto.response.PaymentResponse;
import com.github.alfredobaptista.infrastructure.mapper.PaymentMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final GetPaymentByIdUseCase getPaymentByIdUseCase;
    private final PaymentMapper paymentMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> processPayment(
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader("X-Idempotency-Key") @NotBlank String idempotencyKey
    ) {
        log.debug("Pedido de pagamento recebido — Key: {}, Valor: {}",
                idempotencyKey, request.amount());
        
        CreatePaymentInput input = new CreatePaymentInput(
                idempotencyKey,
                request.amount(),
                request.currency(),
                request.customerId(),
                request.description()
        );

        Payment payment = processPaymentUseCase.execute(input);

        PaymentResponse response = paymentMapper.toResponse(payment);

        log.info("Pagamento processado — Key: {}, Status: {}",
                idempotencyKey, response.status());

        return ResponseEntity.ok(
                ApiResponse.ok("Pagamento processado com sucesso", response)
        );
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable UUID paymentId
    ) {
        log.debug("Consulta de pagamento — ID: {}", paymentId);

        Payment payment = getPaymentByIdUseCase.getPaymentById(paymentId);
        PaymentResponse response = paymentMapper.toResponse(payment);

        return ResponseEntity.ok(
                ApiResponse.ok("Pagamento encontrado", response)
        );
    }
}