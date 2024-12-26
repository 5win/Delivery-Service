package com.oheat.food.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OptionUpdateRequest {

    private final Long optionId;
    private final String name;
    private final int price;
}
