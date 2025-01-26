package com.oheat.user.dto;

import com.oheat.common.sido.Sido;
import com.oheat.common.sigungu.Sigungu;
import com.oheat.user.entity.Address;
import com.oheat.user.entity.UserJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class AddressSaveRequest {

    private final String address;
    private final String detailAddress;
    private final String sido;
    private final String sigungu;
    private final Double latitude;
    private final Double longitude;
    private final String nickname;
    private final boolean selected;

    public Address toEntity(UserJpaEntity user, Sido sido, Sigungu sigungu) {
        return Address.builder()
            .address(address)
            .detailAddress(detailAddress)
            .latitude(latitude)
            .longitude(longitude)
            .nickname(nickname)
            .selected(selected)
            .user(user)
            .sido(sido)
            .sigungu(sigungu)
            .build();
    }
}
