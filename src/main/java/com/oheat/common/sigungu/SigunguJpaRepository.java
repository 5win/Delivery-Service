package com.oheat.common.sigungu;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigunguJpaRepository extends JpaRepository<Sigungu, Long> {

    Optional<Sigungu> findBySigKorNm(String sigKorNm);
}
