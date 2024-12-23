package com.oheat.user.dto;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.user.entity.CartJpaEntity;
import com.oheat.user.entity.UserJpaEntity;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CartSaveRequest {

    private final Long shopId;
    private final Long menuId;
    private final List<CartOptionGroupSaveRequest> optionGroups;
    private final int amount;

    public CartJpaEntity toEntity(UserJpaEntity user, ShopJpaEntity shop, MenuJpaEntity menu) {
        return CartJpaEntity.builder()
            .amount(this.amount)
            .user(user)
            .shop(shop)
            .menu(menu)
            .build();
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class CartOptionGroupSaveRequest {

        private final Long optionGroupId;
        private final List<Long> options;
    }
}
