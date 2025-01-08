package com.oheat.order.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class TossPaymentConfirmRequest {

    private final UUID orderId;
    private final int amount;
    private final String paymentKey;
}
