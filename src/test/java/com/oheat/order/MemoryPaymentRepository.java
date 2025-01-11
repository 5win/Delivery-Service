package com.oheat.order;

import com.oheat.order.entity.Payment;
import com.oheat.order.repository.PaymentRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryPaymentRepository implements PaymentRepository {

    private final Map<String, Payment> payments = new HashMap<>();

    @Override
    public void save(Payment payment) {
        payments.put(payment.getPaymentKey(), payment);
    }

    @Override
    public Optional<Payment> findById(String paymentKey) {
        return Optional.ofNullable(payments.get(paymentKey));
    }
}
