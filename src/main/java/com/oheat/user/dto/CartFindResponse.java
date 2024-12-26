package com.oheat.user.dto;

import com.oheat.user.entity.CartJpaEntity;
import com.oheat.user.entity.CartOptionGroup;
import com.oheat.user.entity.CartOptionGroupOption;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CartFindResponse {

    private final Long cartId;
    private final Long shopId;
    private final String shopName;
    private final Long menuId;
    private final String menuName;
    private final int price;
    private final int amount;
    private final List<OptionGroupInfo> optionGroups;

    public static CartFindResponse from(CartJpaEntity cart) {
        return CartFindResponse.builder()
            .cartId(cart.getId())
            .shopId(cart.getShop().getId())
            .shopName(cart.getShop().getName())
            .menuId(cart.getMenu().getId())
            .menuName(cart.getMenu().getName())
            .price(cart.calcPriceOfMenu())
            .amount(cart.getAmount())
            .optionGroups(OptionGroupInfo.from(cart.getCartOptionGroups()))
            .build();
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    private static class OptionGroupInfo {

        private final Long optionGroupId;
        private final String optionGroupName;
        private final List<OptionInfo> options;

        public static List<OptionGroupInfo> from(List<CartOptionGroup> cartOptionGroups) {
            return cartOptionGroups.stream()
                .map(cartGroup -> OptionGroupInfo.builder()
                    .optionGroupId(cartGroup.getId())
                    .optionGroupName(cartGroup.getOptionGroup().getName())
                    .options(OptionInfo.from(cartGroup.getCartOptionGroupOptions()))
                    .build())
                .toList();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    private static class OptionInfo {

        private final Long optionId;
        private final String optionName;

        public static List<OptionInfo> from(List<CartOptionGroupOption> cartOptionGroupOptions) {
            return cartOptionGroupOptions.stream()
                .map(cartOption -> OptionInfo.builder()
                    .optionId(cartOption.getId())
                    .optionName(cartOption.getOption().getName())
                    .build())
                .toList();
        }
    }
}
