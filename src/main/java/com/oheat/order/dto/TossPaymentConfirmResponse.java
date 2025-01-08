package com.oheat.order.dto;

import com.oheat.order.constant.PaymentState;
import com.oheat.order.entity.Payment;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class TossPaymentConfirmResponse {

    private final UUID orderId;
    private final String mId;
    private final String lastTransactionKey;
    private final String paymentKey;
    private final String orderName;
    private final int totalAmount;
    private final OffsetDateTime requestedAt;
    private final OffsetDateTime approvedAt;

    public Payment toEntity() {
        return Payment.builder()
            .paymentKey(paymentKey)
            .orderId(orderId)
            .totalAmount(totalAmount)
            .state(PaymentState.CONFIRMED)
            .build();
    }
}
