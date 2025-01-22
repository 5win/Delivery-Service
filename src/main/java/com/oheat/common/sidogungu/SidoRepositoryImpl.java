package com.oheat.common.sidogungu;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SidoRepositoryImpl implements SidoRepository {

    private final SidoJpaRepository jpaRepository;

    @Override
    public Optional<Sido> findByCtpKorNm(String ctpKorNm) {
        return Optional.empty();
    }
}
