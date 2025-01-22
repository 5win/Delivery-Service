package com.oheat.common.sigungu;

import java.util.Optional;

public interface SigunguRepository {

    Optional<Sigungu> findBySigKorNm(String sigKorNm);
}
