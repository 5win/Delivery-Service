package com.oheat.order.service;

import com.oheat.order.dto.TossPaymentConfirmResponse;
import com.oheat.order.entity.Payment;
import com.oheat.order.exception.DuplicatePaymentKeyException;
import com.oheat.order.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TossPaymentService {

    private final PaymentRepository paymentRepository;
    private final TossPaymentClient tossPaymentClient;

    @Transactional
    public ResponseEntity<TossPaymentConfirmResponse> confirm(Payment payment) {
        paymentRepository.findById(payment.getPaymentKey())
            .ifPresent(v -> {
                throw new DuplicatePaymentKeyException();
            });

        var response = tossPaymentClient.confirmPayment(payment);
        if (response.getStatusCode().is2xxSuccessful()) {
            payment.setConfirmed();
        }
        paymentRepository.save(payment);

        return response;
    }
}
