package com.github.alfredobaptista.infrastructure.repository;

import com.github.alfredobaptista.infrastructure.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentModel, UUID> {
}
