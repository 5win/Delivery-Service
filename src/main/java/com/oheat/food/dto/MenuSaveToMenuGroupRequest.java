package com.oheat.food.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuSaveToMenuGroupRequest {

    private final Long menuGroupId;
    private final Long menuId;
}
