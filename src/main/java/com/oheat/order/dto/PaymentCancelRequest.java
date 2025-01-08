package com.oheat.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class PaymentCancelRequest {

    private final String paymentKey;
    private final String cancelReason;
}
