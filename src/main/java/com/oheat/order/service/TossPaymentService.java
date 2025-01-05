package com.oheat.order.service;

import com.oheat.order.dto.TossPaymentConfirmResponse;
import com.oheat.order.entity.Payment;
import com.oheat.order.exception.DuplicatePaymentKeyException;
import com.oheat.order.exception.InvalidPaymentInfoException;
import com.oheat.order.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ResponseEntity<TossPaymentConfirmResponse> confirm(Payment paymentFromTossPayments) {
        Payment paymentFromClient = paymentRepository.findById(paymentFromTossPayments.getPaymentKey())
            .orElseThrow(() -> new InvalidPaymentInfoException(HttpStatus.BAD_REQUEST, "존재하지 않는 결제 정보입니다."));

        if (!paymentFromClient.equals(paymentFromTossPayments)) {
            throw new InvalidPaymentInfoException(HttpStatus.BAD_REQUEST, "결제 정보가 유효하지 않습니다.");
        }
        paymentFromClient.setPaid(true);
        return paymentConfirmClient.confirmTossPayment(paymentFromTossPayments);
    }
}
