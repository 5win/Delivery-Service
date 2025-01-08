package com.oheat.order.service;

import com.oheat.order.dto.PaymentCancelRequest;
import com.oheat.order.dto.TossPaymentConfirmRequest;
import com.oheat.order.entity.Payment;
import com.oheat.order.exception.DuplicatePaymentKeyException;
import com.oheat.order.exception.PaymentNotExistsException;
import com.oheat.order.repository.PaymentRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TossPaymentService {

    private final PaymentRepository paymentRepository;
    private final TossPaymentClient tossPaymentClient;

    @Transactional
    public void confirm(TossPaymentConfirmRequest confirmRequest) {
        paymentRepository.findById(confirmRequest.getPaymentKey())
            .ifPresent(v -> {
                throw new DuplicatePaymentKeyException();
            });

        var response = tossPaymentClient.confirmPayment(confirmRequest);
        Payment payment = Objects.requireNonNull(response.getBody())
            .toEntity();

        if (response.getStatusCode().is2xxSuccessful()) {
            payment.setConfirmed();
        }
        paymentRepository.save(payment);
    }

    @Transactional
    public void cancel(PaymentCancelRequest cancelRequest) {
        Payment payment = paymentRepository.findById(cancelRequest.getPaymentKey())
            .orElseThrow(() -> new PaymentNotExistsException(HttpStatus.BAD_REQUEST, "존재하지 않는 결제입니다."));

        tossPaymentClient.cancelPayment(cancelRequest.getPaymentKey(), cancelRequest.getCancelReason());
        payment.setCancelled();
    }
}
