package com.oheat.order.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class TossPaymentResponse {

    private final UUID orderId;
    private final String mId;
    private final String lastTransactionKey;
    private final String paymentKey;
    private final String orderName;
    private final int totalAmount;
    private final OffsetDateTime requestedAt;
    private final OffsetDateTime approvedAt;
}
