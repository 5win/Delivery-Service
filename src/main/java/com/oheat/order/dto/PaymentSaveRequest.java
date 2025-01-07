package com.oheat.order.dto;

import com.oheat.order.constant.PaymentState;
import com.oheat.order.entity.Payment;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PaymentSaveRequest {

    private final UUID orderId;
    private final int amount;
    private final String paymentKey;

    public Payment toEntity() {
        return Payment.builder()
            .orderId(orderId)
            .amount(amount)
            .paymentKey(paymentKey)
            .state(PaymentState.UNCONFIRMED)
            .build();
    }
}
