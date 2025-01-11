package com.oheat.food.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ShopUpdateRequest {

    private final Long id;
    private final String name;
    private final String phone;
    private final String category;
    private final int minimumOrderAmount;
    private final int deliveryFee;
    private final Double latitude;
    private final Double longitude;
}
