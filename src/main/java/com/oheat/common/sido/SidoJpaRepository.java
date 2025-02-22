package com.oheat.common.sido;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SidoJpaRepository extends JpaRepository<Sido, Long> {

    Optional<Sido> findByCtpKorNm(String ctpKorNm);
}
