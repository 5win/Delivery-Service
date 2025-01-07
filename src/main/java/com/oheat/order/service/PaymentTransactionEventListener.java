package com.oheat.order.service;

import com.oheat.order.entity.Payment;
import com.oheat.order.exception.PaymentNotExistsException;
import com.oheat.order.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentTransactionEventListener {

    private final PaymentRepository paymentRepository;

    private final TossPaymentClient tossPaymentClient;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void paymentCancelEventListener(String paymentKey) {
        log.error("주문 과정에서 문제가 발생하여, 결제 취소를 요청합니다.");

        Payment payment = paymentRepository.findById(paymentKey)
            .orElseThrow(() -> new PaymentNotExistsException(HttpStatus.BAD_REQUEST, "결제 정보가 존재하지 않습니다."));

        var response = tossPaymentClient.cancelPayment(paymentKey, "주문 오류로 인한 결제 취소.");
        if (response.getStatusCode() == HttpStatus.OK) {
            payment.setCancelled();
        }
    }
}
