package com.oheat.common;

import com.oheat.common.sido.Sido;
import com.oheat.common.sigungu.Sigungu;

public class SidogunguFixture {

    public static Sido seoul() {
        return Sido.builder()
            .ogrFid(1)
            .geometry(null)
            .ctprvnCd("11")
            .ctpKorNm("서울특별시")
            .ctpEngNm("Seoul")
            .build();
    }

    public static Sigungu jongno_gu() {
        return Sigungu.builder()
            .ogrFid(1)
            .geometry(null)
            .sigCd("11110")
            .sigKorNm("종로구")
            .sigEngNm("Jongno-gu")
            .build();
    }
}
