package com.oheat.food.jpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.repository.CategoryJpaRepository;
import com.oheat.food.repository.ShopJpaRepository;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class ShopRepositoryTest {

    @Autowired
    private ShopJpaRepository shopJpaRepository;
    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Test
    @DisplayName("매장 등록 정보에 카테고리가 null이면 등록 실패")
    void shopNotContainsCategory_thenFail() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            shopJpaRepository.save(
                ShopJpaEntity.builder()
                    .name("bbq")
                    .minimumOrderAmount(14_000)
                    .build()
            );
        });
    }

    @Test
    @DisplayName("이름이 중복되지 않으면, 매장 등록 성공")
    void shopNameNotDuplicate_thenSuccess() {
        categoryJpaRepository.save(
            CategoryJpaEntity.builder()
                .name("치킨")
                .build()
        );
        CategoryJpaEntity category = categoryJpaRepository.findByName("치킨").get();

        assertDoesNotThrow(() -> {
            shopJpaRepository.save(
                ShopJpaEntity.builder()
                    .name("bbq")
                    .category(category)
                    .minimumOrderAmount(14_000)
                    .build());
        });
    }

    @Test
    @DisplayName("이름이 중복되면, 매장 등록 실패")
    void shopNameDuplicate_thenFail() {
        categoryJpaRepository.save(
            CategoryJpaEntity.builder()
                .name("치킨")
                .build()
        );
        CategoryJpaEntity category = categoryJpaRepository.findByName("치킨").get();

        shopJpaRepository.save(
            ShopJpaEntity.builder()
                .name("bbq")
                .category(category)
                .minimumOrderAmount(14_000)
                .build());

        assertThrows(DataIntegrityViolationException.class, () -> {
            shopJpaRepository.save(
                ShopJpaEntity.builder()
                    .name("bbq")
                    .category(category)
                    .minimumOrderAmount(14_000)
                    .build());
        });
    }

    @Test
    @DisplayName("같은 카테고리의 매장이 3개 있을 때, JPA로 해당 카테고리의 매장 조회 시 size는 3이어야 함")
    void usingJpa_givenThreeShop_whenFindByCategory_thenListSizeThree() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        categoryJpaRepository.save(category);

        for (int i = 0; i < 3; i++) {
            shopJpaRepository.save(ShopJpaEntity.builder()
                .name("bbq " + i + "호점").category(category).build());
        }

        List<ShopJpaEntity> result = shopJpaRepository.findByCategory(category);

        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("치킨 매장이 7개이고 5개 단위로 페이징할 때, 조회 결과는 0번 페이지는 5개, 1번 페이지는 2개이다")
    void givenSevenShops_whenFindShopByCategory_thenPage0Return5AndPage1Return2() {

    }

    @Disabled
    @Test
    @DisplayName("카테고리로 매장 목록 조회 시 정렬 기준이 기본순이면, 최근 등록 순으로 조회")
    void whenSortByDefault_thenRecentRegistrationOrder() {

    }

    @Disabled
    @Test
    @DisplayName("카테고리로 매장 목록 조회 시 정렬 기준이 배달팁 낮은 순이면, 배달팁 오름차순으로 조회")
    void whenSortByDeliveryTip_thenDeliveryTipAscendingOrder() {

    }

    @Disabled
    @Test
    @DisplayName("카테고리로 매장 목록 조회 시 정렬 기준이 최소주문 금액 낮은 순이면, 최소주문 금액 오름차순으로 조회")
    void whenSortByMinimumOrderAmount_thenMinimumOrderAmountAscendingOrder() {

    }

    @Disabled
    @Test
    @DisplayName("카테고리로 매장 목록 조회 시 정렬 기준이 가까운 순이면, 내 위치에서의 거리 오름차순으로 조회")
    void whenSortByNearestOrder_thenDistanceInMyLocationAscendingOrder() {

    }

    @Disabled
    @Test
    @DisplayName("카테고리로 매장 목록 조회 시 정렬 기준이 주문 많은 순이면, 누적 주문 수 내림차순으로 조회")
    void whenSortByOrderAmount_thenOrderAmountDescendingOrder() {

    }

    @Disabled
    @Test
    @DisplayName("카테고리로 매장 목록 조회 시 정렬 기준이 별점 높은 순이면, 별점 내림차순으로 조회")
    void whenSortByReviewScore_thenReviewScoreDescendingOrder() {

    }
}
