package com.oheat.food.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuUpdateRequest {

    private final Long menuId;
    private final String name;
    private final int price;
    private final Long shopId;
}
