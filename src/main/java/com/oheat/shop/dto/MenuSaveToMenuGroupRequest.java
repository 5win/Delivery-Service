package com.oheat.shop.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuSaveToMenuGroupRequest {

    private final Long menuGroupId;
    private final List<Long> menuList;
}
