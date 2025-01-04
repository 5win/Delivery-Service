package com.oheat.order.dto;

import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.order.constant.OrderState;
import com.oheat.order.constant.PayMethod;
import com.oheat.order.entity.Order;
import com.oheat.order.entity.Payment;
import com.oheat.user.entity.UserJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OrderSaveRequest {

    private final String paymentKey;
    private final String msgForShop;
    private final int deliveryFee;
    private final int discount;
    private final PayMethod payMethod;

    public Order toEntity(ShopJpaEntity shop, UserJpaEntity user, Payment payment) {
        return Order.builder()
            .orderState(OrderState.PENDING)
            .address(user.getAddress())
            .phone(user.getPhone())
            .msgForShop(this.msgForShop)
            .deliveryFee(this.deliveryFee)
            .discount(this.discount)
            .payMethod(this.payMethod)
            .reviewed(false)
            .shop(shop)
            .user(user)
            .payment(payment)
            .build();
    }
}
