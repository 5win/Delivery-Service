package com.oheat.food.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.oheat.food.dto.MenuGroupSaveRequest;
import com.oheat.food.dto.MenuGroupUpdateRequest;
import com.oheat.food.dto.MenuSaveToMenuGroupRequest;
import com.oheat.food.entity.MenuGroupJpaEntity;
import com.oheat.food.entity.MenuGroupMappingJpaEntity;
import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.exception.DuplicateMenuException;
import com.oheat.food.exception.MenuGroupNotExistsException;
import com.oheat.food.exception.MenuNotExistsException;
import com.oheat.food.exception.ShopNotExistsException;
import com.oheat.food.fake.MemoryMenuGroupMappingRepository;
import com.oheat.food.fake.MemoryMenuGroupRepository;
import com.oheat.food.fake.MemoryMenuRepository;
import com.oheat.food.fake.MemoryShopRepository;
import com.oheat.food.repository.MenuGroupMappingRepository;
import com.oheat.food.repository.MenuGroupRepository;
import com.oheat.food.repository.MenuRepository;
import com.oheat.food.repository.ShopRepository;
import com.oheat.food.service.MenuGroupService;
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
                .menuGroupId(1L).menuId(1L).build());
        });
    }

    @Test
    @DisplayName("존재하지 않는 메뉴이면, 메뉴 그룹에 추가 실패")
    void givenWrongMenu_whenAddToMenuGroup_thenFail() {
        memoryMenuGroupRepository.save(MenuGroupJpaEntity.builder()
            .name("후라이드").build());

        Assertions.assertThrows(MenuNotExistsException.class, () -> {
            menuGroupService.registerMenuToMenuGroup(MenuSaveToMenuGroupRequest.builder()
                .menuGroupId(1L).menuId(1L).build());
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
                .menuGroupId(1L).menuId(1L).build());
        });
    }

    @Test
    @DisplayName("메뉴 그룹에 메뉴가 이미 존재하면, 메뉴 그룹에 추가 실패")
    void givenAlreadyExistsMenuInGroup_whenAddToMenuGroup_thenFail() {
        memoryMenuGroupRepository.save(MenuGroupJpaEntity
            .builder().name("후라이드").build());
        memoryMenuRepository.save(MenuJpaEntity.builder().name("황올").build());

        menuGroupService.registerMenuToMenuGroup(MenuSaveToMenuGroupRequest.builder()
            .menuGroupId(1L).menuId(1L).build());

        Assertions.assertThrows(DuplicateMenuException.class, () -> {
            menuGroupService.registerMenuToMenuGroup(MenuSaveToMenuGroupRequest.builder()
                .menuGroupId(1L).menuId(1L).build());
        });
    }

    @Test
    @DisplayName("매장이 존재하지 않으면, 메뉴 그룹 조회 실패")
    void givenWrongShopId_whenFindMenuGroupByShopId_thenFail() {
        Assertions.assertThrows(ShopNotExistsException.class, () -> {
            menuGroupService.findMenuGroupByShopId(1L);
        });
    }

    @Test
    @DisplayName("메뉴 그룹이 2개 있고 각 그룹에 메뉴가 2개씩 존재하면, 총 조회된 메뉴 개수는 4개")
    void givenTwoMenuGroupAndFourMenu_whenFindMenuFromGroup_thenFour() {
        // 메뉴와 메뉴 그룹 생성
        MenuJpaEntity menu1 = MenuJpaEntity.builder().name("황올").build();
        MenuJpaEntity menu2 = MenuJpaEntity.builder().name("자메이카").build();
        MenuJpaEntity menu3 = MenuJpaEntity.builder().name("뿌링클").build();
        MenuJpaEntity menu4 = MenuJpaEntity.builder().name("볼케이노").build();
        MenuGroupJpaEntity menuGroup1 = MenuGroupJpaEntity.builder().name("후라이드").build();
        MenuGroupJpaEntity menuGroup2 = MenuGroupJpaEntity.builder().name("양념").build();

        // 메뉴와 메뉴 그룹 매핑 관계 설정
        MenuGroupMappingJpaEntity mapping1 = MenuGroupMappingJpaEntity.builder()
            .menuGroup(menuGroup1).menu(menu1).build();
        MenuGroupMappingJpaEntity mapping2 = MenuGroupMappingJpaEntity.builder()
            .menuGroup(menuGroup1).menu(menu2).build();
        MenuGroupMappingJpaEntity mapping3 = MenuGroupMappingJpaEntity.builder()
            .menuGroup(menuGroup2).menu(menu3).build();
        MenuGroupMappingJpaEntity mapping4 = MenuGroupMappingJpaEntity.builder()
            .menuGroup(menuGroup2).menu(menu4).build();

        // 각 메뉴 그룹에 메뉴 2개씩 연관관계 매핑
        menuGroup1.addMenuMapping(mapping1);
        menuGroup1.addMenuMapping(mapping2);
        menuGroup2.addMenuMapping(mapping3);
        menuGroup2.addMenuMapping(mapping4);

        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        shop.addMenuGroup(menuGroup1);
        shop.addMenuGroup(menuGroup2);

        memoryShopRepository.save(shop);

        List<MenuGroupJpaEntity> groupResult = menuGroupService.findMenuGroupByShopId(1L);
        int group1Size = groupResult.getFirst().getMenuGroupMappingSet().size();
        int group2Size = groupResult.getLast().getMenuGroupMappingSet().size();

        // 메뉴 그룹의 개수는 2개
        assertThat(groupResult.size()).isEqualTo(2);
        // 총 메뉴의 개수는 4개
        assertThat(group1Size + group2Size).isEqualTo(4);
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
