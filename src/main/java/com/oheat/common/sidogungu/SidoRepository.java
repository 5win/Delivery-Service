package com.oheat.common.sidogungu;

import java.util.Optional;

public interface SidoRepository {

    Optional<Sido> findByCtpKorNm(String ctpKorNm);
}
