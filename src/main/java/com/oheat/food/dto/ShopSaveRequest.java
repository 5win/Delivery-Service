package com.oheat.food.dto;

import com.oheat.common.sido.Sido;
import com.oheat.common.sigungu.Sigungu;
import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ShopSaveRequest {

    private final String name;
    private final String phone;
    private final String category;
    private final int minimumOrderAmount;
    private final int deliveryFee;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final String sido;
    private final String sigungu;

    public ShopJpaEntity toEntity(CategoryJpaEntity category, Sido sido, Sigungu sigungu) {
        return ShopJpaEntity.builder()
            .name(this.name)
            .phone(this.phone)
            .category(category)
            .minimumOrderAmount(this.minimumOrderAmount)
            .deliveryFee(this.deliveryFee)
            .address(this.address)
            .latitude(this.latitude)
            .longitude(this.longitude)
            .sido(sido)
            .sigungu(sigungu)
            .build();
    }
}
