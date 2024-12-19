package com.oheat.food.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OptionGroupUpdateRequest {

    private final Long optionGroupId;
    private final String name;
    private final boolean required;
    private final int maxNumOfSelect;
}
