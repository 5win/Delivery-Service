package com.oheat.user.dto;

import com.oheat.user.entity.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class AddressFindResponse {

    private final Long id;
    private final String address;
    private final String detailAddress;
    private final String sido;
    private final String sigungu;
    private final Double latitude;
    private final Double longitude;
    private final String nickname;
    private final boolean selected;

    public static AddressFindResponse from(Address address) {
        return AddressFindResponse.builder()
            .id(address.getId())
            .address(address.getAddress())
            .detailAddress(address.getDetailAddress())
            .sido(address.getSido().getCtpKorNm())
            .sigungu(address.getSigungu().getSigKorNm())
            .latitude(address.getLatitude())
            .longitude(address.getLongitude())
            .nickname(address.getNickname())
            .selected(address.isSelected())
            .build();
    }
}
