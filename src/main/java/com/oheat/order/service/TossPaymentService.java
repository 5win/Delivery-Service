package com.oheat.order.service;

import com.oheat.order.dto.TossPaymentResponse;
import com.oheat.order.entity.Payment;
import com.oheat.order.exception.DuplicatePaymentKeyException;
import com.oheat.order.exception.InvalidPaymentInfoException;
import com.oheat.order.repository.PaymentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TossPaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentConfirmClient paymentConfirmClient;

    public void savePaymentInfo(Payment payment) {
        paymentRepository.findById(payment.getPaymentKey())
            .ifPresent(v -> {
                throw new DuplicatePaymentKeyException();
            });

        paymentRepository.save(payment);
    }

    public ResponseEntity<TossPaymentResponse> confirm(Payment paymentFromTossPayments) {
        Optional<Payment> paymentFromClient = paymentRepository.findById(
            paymentFromTossPayments.getPaymentKey());

        if (paymentFromClient.isEmpty() || !paymentFromClient.get().equals(paymentFromTossPayments)) {
            throw new InvalidPaymentInfoException(HttpStatus.BAD_REQUEST, "결제 정보가 유효하지 않습니다.");
        }
        return paymentConfirmClient.confirmTossPayment(paymentFromTossPayments);
    }
}
