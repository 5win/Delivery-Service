package com.oheat.common.sigungu;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SigunguRepositoryImpl implements SigunguRepository {

    private final SigunguJpaRepository sigunguJpaRepository;

    @Override
    public Optional<Sigungu> findBySigKorNm(String sigKorNm) {
        return sigunguJpaRepository.findBySigKorNm(sigKorNm);
    }
}
