package com.oheat.shop;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.oheat.shop.dto.ShopSaveRequest;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.exception.CategoryNotExistsException;
import com.oheat.shop.exception.DuplicateShopNameException;
import com.oheat.shop.repository.CategoryRepository;
import com.oheat.shop.repository.ShopRepository;
import com.oheat.shop.service.CategoryService;
import com.oheat.shop.service.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ShopCRUDTest {

    private ShopService shopService;
    private CategoryService categoryService;
    private final ShopRepository memoryShopRepository = new MemoryShopRepository();
    private final CategoryRepository memoryCategoryRepository = new MemoryCategoryRepository();

    @BeforeEach
    void setUp() {
        shopService = new ShopService(memoryShopRepository, memoryCategoryRepository);
        categoryService = new CategoryService(memoryCategoryRepository);
    }

    @Test
    @DisplayName("매장 이름 중복되지 않으면 매장 등록 성공")
    void shopNameNotDuplicate_thenSuccess() {
        categoryService.registerCategory("치킨");

        assertDoesNotThrow(() -> {
            shopService.registerShop(
                ShopSaveRequest.builder()
                    .shopName("bbq")
                    .categoryName("치킨")
                    .build()
            );
        });
    }

    @Test
    @DisplayName("매장 이름이 중복되면 매장 등록 실패")
    void shopNameDuplicate_thenFail() {
        memoryShopRepository.save(
            ShopJpaEntity.builder()
                .name("오잇")
                .build());

        assertThrows(DuplicateShopNameException.class, () -> {
            shopService.registerShop(
                ShopSaveRequest.builder()
                    .shopName("오잇")
                    .build());
        });
    }

    @Test
    @DisplayName("카테고리가 null이면 매장 등록 실패")
    void whenCategoryIsNull_thenFail() {
        assertThrows(RuntimeException.class, () -> {
            shopService.registerShop(ShopSaveRequest.builder()
                .shopName("bbq")
                .categoryName(null)
                .minimumOrderAmount(14_000)
                .build());
        });
    }

    @Test
    @DisplayName("존재하지 않는 카테고리이면 매장 등록 실패")
    void whenCategoryNotExists_thenFail() {
        assertThrows(CategoryNotExistsException.class, () -> {
            shopService.registerShop(ShopSaveRequest.builder()
                .shopName("bbq")
                .categoryName("치킨")
                .minimumOrderAmount(14_000)
                .build());
        });
    }

    @Disabled
    @Test
    @DisplayName("매장 이름에 숫자, 한글, 영어 이외의 글자가 포함되면 실패")
    void shopNameContainsOnlyNumberKoreanEnglish() {

    }

    @Disabled
    @Test
    @DisplayName("치킨 매장 3개를 등록하고 치킨 카테고리에 해당하는 매장을 조회하면, 리스트 size가 3이어야 함")
    void givenThreeChickenShops_whenFindAllChickenShop_thenListSizeThree() {

    }

    @Disabled
    @Test
    @DisplayName("카테고리로 매장 목록 조회 시, 각 매장의 상호명, 배달팁, 최소주문금액 정보 표시")
    void whenFindChickenShop_thenReturnTitleAndDeliveryTipAndMinimumOrderAmount() {

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

    @Disabled
    @Test
    @DisplayName("매장 상세 조회 시, 상호명, 최소주문 금액, 전화번호, 배달팁, 메뉴명, 메뉴 설명, 메뉴 가격 조회")
    void whenFindShop_thenReturnDetailInfo() {

    }
}
