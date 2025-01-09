package com.oheat.food.serviceTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.oheat.food.dto.ShopFindRequest;
import com.oheat.food.dto.ShopFindResponse;
import com.oheat.food.dto.ShopSaveRequest;
import com.oheat.food.dto.ShopUpdateRequest;
import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.exception.CategoryNotExistsException;
import com.oheat.food.exception.DuplicateShopNameException;
import com.oheat.food.exception.ShopNotExistsException;
import com.oheat.food.fake.MemoryCategoryRepository;
import com.oheat.food.fake.MemoryShopRepository;
import com.oheat.food.repository.CategoryRepository;
import com.oheat.food.repository.ShopRepository;
import com.oheat.food.service.ShopService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ShopServiceTest {

    private ShopService shopService;
    private final ShopRepository memoryShopRepository = new MemoryShopRepository();
    private final CategoryRepository memoryCategoryRepository = new MemoryCategoryRepository();

    @BeforeEach
    void setUp() {
        shopService = new ShopService(memoryShopRepository, memoryCategoryRepository);
    }

    @Test
    @DisplayName("매장 이름 중복되지 않으면 매장 등록 성공")
    void shopNameNotDuplicate_thenSuccess() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        memoryCategoryRepository.save(category);

        assertDoesNotThrow(() -> {
            shopService.registerShop(
                ShopSaveRequest.builder()
                    .name("bbq")
                    .category("치킨")
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
                    .name("오잇")
                    .build());
        });
    }

    @Test
    @DisplayName("카테고리가 null이면 매장 등록 실패")
    void whenCategoryIsNull_thenFail() {
        assertThrows(RuntimeException.class, () -> {
            shopService.registerShop(ShopSaveRequest.builder()
                .name("bbq")
                .category(null)
                .minimumOrderAmount(14_000)
                .build());
        });
    }

    @Test
    @DisplayName("존재하지 않는 카테고리이면 매장 등록 실패")
    void whenCategoryNotExists_thenFail() {
        assertThrows(CategoryNotExistsException.class, () -> {
            shopService.registerShop(ShopSaveRequest.builder()
                .name("bbq")
                .category("치킨")
                .minimumOrderAmount(14_000)
                .build());
        });
    }

    @Disabled
    @Test
    @DisplayName("매장 이름에 숫자, 한글, 영어 이외의 글자가 포함되면 실패")
    void shopNameContainsOnlyNumberKoreanEnglish() {

    }

    @Test
    @DisplayName("치킨 매장 3개를 등록하고 치킨 카테고리에 해당하는 매장을 조회하면, 리스트 size가 3이어야 함")
    void givenThreeChickenShops_whenFindChickenShop_thenListSizeThree() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        memoryCategoryRepository.save(category);

        for (int i = 0; i < 3; i++) {
            shopService.registerShop(ShopSaveRequest.builder()
                .name("bbq " + i + "호점")
                .category("치킨")
                .build());
        }

        ShopFindRequest findReq = ShopFindRequest.builder()
            .categoryName("치킨")
            .build();

        PageRequest pageable = PageRequest.of(0, 5, Sort.by("id").descending());
        Page<ShopFindResponse> result = shopService.findShopByCategory(findReq, pageable);

        Assertions.assertThat(result.getContent().size()).isEqualTo(3);
    }

    @Disabled
    @Test
    @DisplayName("카테고리로 매장 목록 조회 시, 각 매장의 상호명, 배달팁, 최소주문금액 정보 표시")
    void whenFindChickenShop_thenReturnTitleAndDeliveryTipAndMinimumOrderAmount() {

    }

    @Disabled
    @Test
    @DisplayName("매장 상세 조회 시, 상호명, 최소주문 금액, 전화번호, 배달팁, 메뉴명, 메뉴 설명, 메뉴 가격 조회")
    void whenFindShop_thenReturnDetailInfo() {

    }

    @Test
    @DisplayName("매장이 존재하지 않으면, 매장 수정 실패")
    void givenWrongShop_whenUpdateShop_thenFail() {
        ShopUpdateRequest updateRequest = ShopUpdateRequest.builder().name("bbq").build();

        assertThrows(ShopNotExistsException.class, () -> {
            shopService.updateShop(updateRequest);
        });
    }

    @Test
    @DisplayName("매장이 존재하지만 카테고리 존재하지 않으면, 매장 수정 실패")
    void givenShopWithWrongCategory_whenUpdateShop_thenFail() {
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        memoryShopRepository.save(shop);

        assertThrows(CategoryNotExistsException.class, () -> {
            shopService.updateShop(ShopUpdateRequest.builder()
                .id(1L).name("bbq").category("치킨").build());
        });
    }

    @Test
    @DisplayName("매장과 카테고리가 존재하면, 매장 수정 성공")
    void whenUpdateShop_thenSuccess() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();

        memoryCategoryRepository.save(category);
        memoryShopRepository.save(shop);

        assertDoesNotThrow(() -> {
            shopService.updateShop(ShopUpdateRequest.builder()
                .id(1L).name("bbq").category("치킨").build());
        });
    }

    @Test
    @DisplayName("매장이 존재하지 않으면, 매장 삭제 실패")
    void givenWrongShop_whenDeleteShop_thenFail() {
        assertThrows(ShopNotExistsException.class, () -> {
            shopService.deleteShop(1L);
        });
    }

    @Test
    @DisplayName("매장이 존재하면, 매장 삭제 성공")
    void whenDeleteShop_thenSuccess() {
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        memoryShopRepository.save(shop);

        assertDoesNotThrow(() -> {
            shopService.deleteShop(1L);
        });
    }
}
