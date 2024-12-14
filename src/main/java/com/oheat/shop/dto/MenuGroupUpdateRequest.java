package com.oheat.shop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuGroupUpdateRequest {

    private final Long menuGroupId;
    private final String name;
}
