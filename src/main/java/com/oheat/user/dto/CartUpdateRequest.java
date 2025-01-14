package com.oheat.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CartUpdateRequest {

    private final Long cartId;
    private final int amount;
}
