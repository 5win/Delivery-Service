package com.oheat.order.dto;

import com.oheat.order.constant.OrderState;
import com.oheat.order.entity.Order;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OrderFindByUserResponse {

    private final UUID id;
    private final Long shopId;
    private final String shopName;
    private final int payAmount;
    private final OrderState orderState;
    private final boolean reviewed;

    public static OrderFindByUserResponse from(Order order) {
        return OrderFindByUserResponse.builder()
            .id(order.getId())
            .shopId(order.getShop().getId())
            .shopName(order.getShop().getName())
            .payAmount(order.calcPayAmount())
            .orderState(order.getOrderState())
            .reviewed(order.isReviewed())
            .build();
    }
}
