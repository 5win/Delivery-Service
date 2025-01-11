package com.oheat.order.repository;

import com.oheat.order.entity.Payment;
import java.util.Optional;

public interface PaymentRepository {

    void save(Payment payment);

    Optional<Payment> findById(String paymentKey);
}
