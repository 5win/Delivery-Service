package com.oheat.shop.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.oheat.shop.dto.MenuGroupSaveRequest;
import com.oheat.shop.dto.MenuGroupUpdateRequest;
import com.oheat.shop.dto.MenuSaveToMenuGroupRequest;
import com.oheat.shop.entity.MenuGroupJpaEntity;
import com.oheat.shop.entity.MenuGroupMappingJpaEntity;
import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.exception.DuplicateMenuException;
import com.oheat.shop.exception.MenuGroupNotExistsException;
import com.oheat.shop.exception.MenuNotExistsException;
import com.oheat.shop.exception.ShopNotExistsException;
import com.oheat.shop.fake.MemoryMenuGroupMappingRepository;
import com.oheat.shop.fake.MemoryMenuGroupRepository;
import com.oheat.shop.fake.MemoryMenuRepository;
import com.oheat.shop.fake.MemoryShopRepository;
import com.oheat.shop.repository.MenuGroupMappingRepository;
import com.oheat.shop.repository.MenuGroupRepository;
import com.oheat.shop.repository.MenuRepository;
import com.oheat.shop.repository.ShopRepository;
import com.oheat.shop.service.MenuGroupService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupCRUDTest {

    private MenuGroupService menuGroupService;
    private final ShopRepository memoryShopRepository = new MemoryShopRepository();
    private final MenuRepository memoryMenuRepository = new MemoryMenuRepository();
    private final MenuGroupMappingRepository memoryMenuGroupMappingRepository = new MemoryMenuGroupMappingRepository();
    private final MenuGroupRepository memoryMenuGroupRepository = new MemoryMenuGroupRepository();

    @BeforeEach
    void setup() {
        menuGroupService = new MenuGroupService(memoryShopRepository, memoryMenuRepository,
            memoryMenuGroupMappingRepository, memoryMenuGroupRepository);
    }

    /**
     * 메뉴 그룹 CRUD 테스트
     */
    @Test
    @DisplayName("매장이 존재하지 않으면, 매장에 새 메뉴 그룹 추가 실패")
    void givenMenuGroupWithWrongShop_whenRegisterMenuGroup_thenFail() {
        Assertions.assertThrows(ShopNotExistsException.class, () -> {
            menuGroupService.registerMenuGroup(MenuGroupSaveRequest.builder()
                .name("후라이드").shopId(1L).build());
        });
    }

    @Test
    @DisplayName("매장이 존재하면, 매장에 새 메뉴 그룹 추가 성공")
    void givenMenuGroup_whenRegisterMenuGroup_thenSuccess() {
        memoryShopRepository.save(ShopJpaEntity.builder().name("bbq").build());

        Assertions.assertDoesNotThrow(() -> {
            menuGroupService.registerMenuGroup(MenuGroupSaveRequest.builder()
                .name("후라이드").shopId(1L).build());
        });
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면, 메뉴 그룹 이름 수정 실패")
    void givenMenuGroupNotExists_whenUpdateMenuGroup_thenFail() {
        MenuGroupUpdateRequest menuGroupUpdateRequest = MenuGroupUpdateRequest.builder()
            .menuGroupId(1L).name("양념치킨").build();

        Assertions.assertThrows(MenuGroupNotExistsException.class, () -> {
            menuGroupService.updateMenuGroup(menuGroupUpdateRequest);
        });
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하면, 메뉴 그룹 이름 수정 성공")
    void givenMenuGroupExists_whenUpdateMenuGroup_thenSuccess() {
        MenuGroupJpaEntity menuGroup = MenuGroupJpaEntity.builder().name("후라이드").build();
        MenuGroupUpdateRequest menuGroupUpdateRequest = MenuGroupUpdateRequest.builder()
            .menuGroupId(1L).name("양념치킨").build();

        memoryMenuGroupRepository.save(menuGroup);

        Assertions.assertDoesNotThrow(() -> {
            menuGroupService.updateMenuGroup(menuGroupUpdateRequest);
        });
        assertThat(memoryMenuGroupRepository.findById(1L).get().getName())
            .isEqualTo("양념치킨");
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면, 메뉴 그룹 삭제 실패")
    void givenMenuGroupNotExists_whenDeleteMenuGroup_thenFail() {
        Assertions.assertThrows(MenuGroupNotExistsException.class, () -> {
            menuGroupService.deleteMenuGroup(1L);
        });
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하면, 메뉴 그룹 삭제 성공")
    void givenMenuGroupExists_whenDeleteMenuGroup_thenSuccess() {
        MenuGroupJpaEntity menuGroup = MenuGroupJpaEntity.builder().name("후라이드").build();

        memoryMenuGroupRepository.save(menuGroup);

        Assertions.assertDoesNotThrow(() -> {
            menuGroupService.deleteMenuGroup(1L);
        });

        if (memoryMenuGroupRepository.findById(1L).isPresent()) {
            fail();
        }
    }

    /**
     * 메뉴 그룹 매핑 CRUD 테스트
     */
    @Test
    @DisplayName("존재하지 않는 메뉴 그룹에 메뉴 추가하면 실패")
    void givenMenuWithWrongMenuGroup_whenAddToMenuGroup_thenFail() {
        Assertions.assertThrows(MenuGroupNotExistsException.class, () -> {
            menuGroupService.registerMenuToMenuGroup(MenuSaveToMenuGroupRequest.builder()
                .menuGroupId(1L).menuList(List.of(1L)).build());
        });
    }

    @Test
    @DisplayName("존재하지 않는 메뉴이면, 메뉴 그룹에 추가 실패")
    void givenWrongMenu_whenAddToMenuGroup_thenFail() {
        memoryMenuGroupRepository.save(MenuGroupJpaEntity.builder()
            .name("후라이드").build());

        Assertions.assertThrows(MenuNotExistsException.class, () -> {
            menuGroupService.registerMenuToMenuGroup(MenuSaveToMenuGroupRequest.builder()
                .menuGroupId(1L).menuList(List.of(1L)).build());
        });
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하면서 메뉴가 이미 추가되어있지 않으면, 메뉴 그룹에 추가 성공")
    void givenNotDuplicateMenuAndMenuGroup_whenAddToMenuGroup_thenSuccess() {
        memoryMenuGroupRepository.save(MenuGroupJpaEntity.builder()
            .name("후라이드").build());
        memoryMenuRepository.save(MenuJpaEntity.builder().name("황올").build());

        Assertions.assertDoesNotThrow(() -> {
            menuGroupService.registerMenuToMenuGroup(MenuSaveToMenuGroupRequest.builder()
                .menuGroupId(1L).menuList(List.of(1L)).build());
        });
    }

    @Test
    @DisplayName("메뉴 그룹에 메뉴가 이미 존재하면, 메뉴 그룹에 추가 실패")
    void givenAlreadyExistsMenuInGroup_whenAddToMenuGroup_thenFail() {
        memoryMenuGroupRepository.save(MenuGroupJpaEntity
            .builder().name("후라이드").build());
        memoryMenuRepository.save(MenuJpaEntity.builder().name("황올").build());

        Assertions.assertThrows(DuplicateMenuException.class, () -> {
            menuGroupService.registerMenuToMenuGroup(MenuSaveToMenuGroupRequest.builder()
                .menuGroupId(1L).menuList(List.of(1L, 1L)).build());
        });
    }

    @Test
    @DisplayName("존재하지 않는 매핑 관계면, 메뉴 그룹에서 삭제 실패")
    void givenMenuNotExistsInMenuGroup_whenDeleteMenuGroupMapping_thenFail() {
        Long deleteRequest = 1L;

        Assertions.assertThrows(MenuNotExistsException.class, () -> {
            menuGroupService.deleteMenuFromMenuGroup(deleteRequest);
        });
    }

    @Test
    @DisplayName("존재하는 매핑 관계면, 메뉴 그룹에서 삭제 성공")
    void givenMenu_whenDeleteMenuGroupMapping_thenSuccess() {
        MenuGroupJpaEntity menuGroup = MenuGroupJpaEntity.builder().name("후라이드").build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황올").build();
        MenuGroupMappingJpaEntity mapping = MenuGroupMappingJpaEntity.builder()
            .menuGroup(menuGroup).menu(menu).build();

        memoryMenuGroupRepository.save(menuGroup);
        memoryMenuRepository.save(menu);
        memoryMenuGroupMappingRepository.save(mapping);

        Assertions.assertDoesNotThrow(() -> {
            menuGroupService.deleteMenuFromMenuGroup(1L);
        });
        if (memoryMenuGroupMappingRepository.findById(1L).isPresent()) {
            fail();
        }
    }
}
