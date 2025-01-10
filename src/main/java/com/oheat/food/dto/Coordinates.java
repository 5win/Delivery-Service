package com.oheat.food.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class Coordinates {

    private final Double latitude;
    private final Double longitude;

    public static Coordinates from(ShopFindRequest findRequest) {
        return Coordinates.builder()
            .latitude(findRequest.getLatitude())
            .longitude(findRequest.getLongitude())
            .build();
    }
}
