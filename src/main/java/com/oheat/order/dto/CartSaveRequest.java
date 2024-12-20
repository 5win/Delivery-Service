package com.oheat.order.dto;

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

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class CartOptionGroupSaveRequest {

        private final Long optionGroupId;
        private final List<Long> options;
    }
}
