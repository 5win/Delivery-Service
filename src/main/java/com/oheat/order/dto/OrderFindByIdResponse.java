package com.oheat.order.dto;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import com.oheat.food.entity.OptionJpaEntity;
import com.oheat.order.constant.OrderState;
import com.oheat.order.constant.PayMethod;
import com.oheat.order.entity.Order;
import com.oheat.order.entity.OrderMenu;
import com.oheat.order.entity.OrderOption;
import com.oheat.order.entity.OrderOptionGroup;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OrderFindByIdResponse {

    private final Long orderId;
    private final OrderState orderState;
    private final String address;
    private final String phone;
    private final String msgForShop;
    private final int deliveryFee;
    private final int discount;
    private final PayMethod payMethod;
    private final boolean reviewed;
    private final int payAmount;
    private final Long shopId;
    private final String shopName;
    private final List<MenuInfo> menuInfos;

    public static OrderFindByIdResponse from(Order order) {
        return OrderFindByIdResponse.builder()
            .orderId(order.getId())
            .orderState(order.getOrderState())
            .address(order.getAddress())
            .phone(order.getPhone())
            .msgForShop(order.getMsgForShop())
            .deliveryFee(order.getDeliveryFee())
            .discount(order.getDiscount())
            .payMethod(order.getPayMethod())
            .reviewed(order.isReviewed())
            .payAmount(order.calcPayAmount())
            .shopId(order.getShop().getId())
            .shopName(order.getShop().getName())
            .menuInfos(order.getOrderMenus().stream()
                .map(MenuInfo::from)
                .toList())
            .build();
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    private static class MenuInfo {

        private final Long menuId;
        private final String menuName;
        private final int amount;
        private final int totalPrice;
        private final List<OptionGroupInfo> optionGroupInfos;

        public static MenuInfo from(OrderMenu orderMenu) {
            MenuJpaEntity menu = orderMenu.getMenu();

            return MenuInfo.builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .amount(orderMenu.getAmount())
                .totalPrice(orderMenu.calcTotalPrice())
                .optionGroupInfos(orderMenu.getOrderOptionGroups().stream()
                    .map(OptionGroupInfo::from)
                    .toList())
                .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    private static class OptionGroupInfo {

        private final Long optionGroupId;
        private final String optionGroupName;
        private final List<OptionInfo> optionInfos;

        public static OptionGroupInfo from(OrderOptionGroup orderOptionGroup) {
            OptionGroupJpaEntity optionGroup = orderOptionGroup.getOptionGroup();

            return OptionGroupInfo.builder()
                .optionGroupId(optionGroup.getId())
                .optionGroupName(optionGroup.getName())
                .optionInfos(orderOptionGroup.getOrderOptions().stream()
                    .map(OptionInfo::from)
                    .toList())
                .build();
        }
    }

    @Getter
    @Builder
    @RequiredArgsConstructor
    private static class OptionInfo {

        private final Long optionId;
        private final String optionName;
        private final int optionPrice;

        public static OptionInfo from(OrderOption orderOption) {
            OptionJpaEntity option = orderOption.getOption();

            return OptionInfo.builder()
                .optionId(option.getId())
                .optionName(option.getName())
                .optionPrice(option.getPrice())
                .build();
        }
    }
}
