package com.oheat.shop.serviceTest;

import com.oheat.shop.dto.MenuGroupSaveRequest;
import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.exception.DuplicateMenuException;
import com.oheat.shop.exception.MenuNotExistsException;
import com.oheat.shop.exception.ShopNotExistsException;
import com.oheat.shop.fake.MemoryMenuGroupRepository;
import com.oheat.shop.fake.MemoryMenuRepository;
import com.oheat.shop.fake.MemoryShopRepository;
import com.oheat.shop.repository.MenuGroupRepository;
import com.oheat.shop.repository.MenuRepository;
import com.oheat.shop.repository.ShopRepository;
import com.oheat.shop.service.MenuGroupService;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupCRUDTest {

    private MenuGroupService menuGroupService;
    private final ShopRepository memoryShopRepository = new MemoryShopRepository();
    private final MenuRepository memoryMenuRepository = new MemoryMenuRepository();
    private final MenuGroupRepository memoryMenuGroupRepository = new MemoryMenuGroupRepository();

    @BeforeEach
    void setup() {
        menuGroupService = new MenuGroupService(memoryShopRepository, memoryMenuRepository,
            memoryMenuGroupRepository);
    }

    @Test
    @DisplayName("존재하지 않는 매장에 메뉴 그룹 추가하면 실패")
    void givenWrongShopId_whenAddToMenuGroup_thenFail() {
        Assertions.assertThrows(ShopNotExistsException.class, () -> {
            menuGroupService.save(MenuGroupSaveRequest.builder()
                .name("후라이드").shopId(1L).menuList(List.of(1L, 2L)).build());
        });
    }

    @Test
    @DisplayName("메뉴 그룹에 메뉴 추가 시, 메뉴가 해당 매장에 속하지 않으면 추가 실패")
    void givenShopIdAndWrongMenuId_whenAddToMenuGroup_thenFail() {
        MenuJpaEntity menu = MenuJpaEntity.builder().name("뿌링클").shopId(2L).build();
        memoryShopRepository.save(ShopJpaEntity.builder().name("bbq").build());
        memoryShopRepository.save(
            ShopJpaEntity.builder().name("bhc").menuSet(Set.of(menu)).build());
        memoryMenuRepository.save(menu);

        Assertions.assertThrows(MenuNotExistsException.class, () -> {
            menuGroupService.save(MenuGroupSaveRequest.builder()
                .name("후라이드").shopId(1L).menuList(List.of(1L)).build());
        });

        Assertions.assertDoesNotThrow(() -> {
            menuGroupService.save(MenuGroupSaveRequest.builder()
                .name("후라이드").shopId(2L).menuList(List.of(1L)).build());
        });
    }

    @Test
    @DisplayName("메뉴 그룹에 메뉴 추가 시, 메뉴가 해당 매장에 속하면서 중복되지 않으면 추가 성공")
    void givenShopIdAndMenuId_whenAddToMenuGroup_thenSuccess() {
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황올").shopId(1L).build();
        memoryShopRepository.save(ShopJpaEntity.builder()
            .name("bbq").menuSet(Set.of(menu)).build());
        memoryMenuRepository.save(menu);

        Assertions.assertDoesNotThrow(() -> {
            menuGroupService.save(MenuGroupSaveRequest.builder()
                .name("후라이드").shopId(1L).menuList(List.of(1L)).build());
        });
    }

    @Test
    @DisplayName("메뉴 그룹에 이미 추가된 메뉴이면, 메뉴 그룹에 메뉴 추가 실패")
    void givenShopIdAndDuplicateMenu_whenAddToMenuGroup_thenFail() {
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황올").shopId(1L).build();
        memoryShopRepository.save(ShopJpaEntity.builder()
            .name("bbq").menuSet(Set.of(menu)).build());
        memoryMenuRepository.save(menu);

        Assertions.assertThrows(DuplicateMenuException.class, () -> {
            menuGroupService.save(MenuGroupSaveRequest.builder()
                .name("후라이드").shopId(1L).menuList(List.of(1L, 1L)).build());
        });
    }
}
