package com.oheat.common.sido;

import java.util.Optional;

public interface SidoRepository {

    Optional<Sido> findByCtpKorNm(String ctpKorNm);
}
