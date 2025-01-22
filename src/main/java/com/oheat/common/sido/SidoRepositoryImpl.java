package com.oheat.common.sido;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SidoRepositoryImpl implements SidoRepository {

    private final SidoJpaRepository sidoJpaRepository;

    @Override
    public Optional<Sido> findByCtpKorNm(String ctpKorNm) {
        return sidoJpaRepository.findByCtpKorNm(ctpKorNm);
    }
}
