package com.oheat.food.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.oheat.food.dto.MenuSaveRequest;
import com.oheat.food.dto.MenuUpdateRequest;
import com.oheat.food.dto.OptionGroupSaveRequest;
import com.oheat.food.dto.OptionGroupUpdateRequest;
import com.oheat.food.dto.OptionSaveRequest;
import com.oheat.food.dto.OptionUpdateRequest;
import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import com.oheat.food.entity.OptionJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.exception.DuplicateMenuException;
import com.oheat.food.exception.MenuNotExistsException;
import com.oheat.food.exception.OptionGroupNotExistsException;
import com.oheat.food.exception.OptionNotExistsException;
import com.oheat.food.exception.ShopNotExistsException;
import com.oheat.food.fake.MemoryMenuRepository;
import com.oheat.food.fake.MemoryOptionGroupRepository;
import com.oheat.food.fake.MemoryOptionRepository;
import com.oheat.food.fake.MemoryShopRepository;
import com.oheat.food.repository.MenuRepository;
import com.oheat.food.repository.OptionGroupRepository;
import com.oheat.food.repository.OptionRepository;
import com.oheat.food.repository.ShopRepository;
import com.oheat.food.service.MenuService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuServiceTest {

    private MenuService menuService;
    private final ShopRepository memoryShopRepository = new MemoryShopRepository();
    private final MenuRepository memoryMenuRepository = new MemoryMenuRepository();
    private final OptionGroupRepository memoryOptionGroupRepository = new MemoryOptionGroupRepository();
    private final OptionRepository memoryOptionRepository = new MemoryOptionRepository();

    @BeforeEach
    void setUp() {
        menuService = new MenuService(memoryShopRepository, memoryMenuRepository,
            memoryOptionGroupRepository, memoryOptionRepository);
    }

    // Menu CRUD Test
    @Test
    @DisplayName("메뉴 등록 시, 매장id에 해당하는 매장이 없다면 메뉴 등록 실패")
    void givenMenuWithoutShopId_whenAddNewMenu_thenFail() {
        Assertions.assertThrows(ShopNotExistsException.class, () -> {
            menuService.registerMenu(MenuSaveRequest.builder()
                .name("황금올리브")
                .shopId(1L).
                build());
        });
    }

    @Test
    @DisplayName("메뉴 등록 시, 1개 이상의 옵션 그룹이 존재하고 각 옵션 그룹에 옵션이 1개 이상이라면 메뉴 등록 성공")
    void givenMenuWithNotEmptyOptionGroup_whenAddNewMenu_thenSuccess() {
        memoryShopRepository.save(ShopJpaEntity.builder().name("bbq").build());

        Assertions.assertDoesNotThrow(() -> {
            menuService.registerMenu(MenuSaveRequest.builder()
                .name("황금올리브")
                .shopId(1L)
                .build());
        });
    }

    @Test
    @DisplayName("메뉴 등록 시, 이미 메뉴가 있다면 메뉴 등록 실패")
    void givenDuplicateMenu_whenAddNewMenu_thenFail() {
        memoryShopRepository.save(ShopJpaEntity.builder().name("bbq").build());
        menuService.registerMenu(MenuSaveRequest.builder()
            .name("황금올리브").shopId(1L).build());

        Assertions.assertThrows(DuplicateMenuException.class, () -> {
            menuService.registerMenu(MenuSaveRequest.builder()
                .name("황금올리브").shopId(1L).build());
        });
    }

    @Test
    @DisplayName("매장에 3개의 메뉴를 등록하면, 메뉴 DB의 size가 3이어야 함")
    void givenThreeMenu_whenAddNewMenu_thenListSizeThree() {
        memoryShopRepository.save(ShopJpaEntity.builder().name("bbq").build());

        for (int i = 0; i < 3; i++) {
            menuService.registerMenu(MenuSaveRequest.builder()
                .name("황올 세트 " + i + "번")
                .shopId(1L)
                .build());
        }

        List<MenuJpaEntity> result = ((MemoryMenuRepository) memoryMenuRepository).findAll();

        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("매장에 3개의 메뉴를 등록한 뒤 매장 엔티티를 조회하면, 매장 엔티티의 메뉴 리스트 사이즈가 3이어야 함")
    void givenThreeMenu_whenAddNewMenuAndFindShopById_thenMenuListAtShopIsSizeThree() {
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        for (int i = 0; i < 3; i++) {
            shop.getMenuSet().add(MenuJpaEntity.builder()
                .name("황올 세트 " + i + "번")
                .shop(shop)
                .build());
        }
        memoryShopRepository.save(shop);

        List<MenuJpaEntity> result = menuService.findByShopId(1L);

        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("해당 매장이 존재하지 않으면, 메뉴 수정 실패")
    void givenWrongShopId_whenUpdateMenu_thenFail() {
        Assertions.assertThrows(ShopNotExistsException.class, () -> {
            menuService.updateMenu(MenuUpdateRequest.builder()
                .menuId(1L)
                .name("황금올리브")
                .build());
        });
    }

    @Test
    @DisplayName("옵션 그룹과 옵션 값이 존재하면, 기존 메뉴 삭제 후 수정된 메뉴로 저장")
    void givenMenuWithNotEmptyOptionGroup_whenUpdateMenu_thenSuccess() {
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황금올리브").build();

        memoryShopRepository.save(shop);
        memoryMenuRepository.save(menu);

        Assertions.assertDoesNotThrow(() -> {
            menuService.updateMenu(MenuUpdateRequest.builder()
                .menuId(1L)
                .shopId(1L)
                .name("양념치킨")
                .build());
        });
        assertThat(memoryMenuRepository.findById(1L).get().getName())
            .isEqualTo("양념치킨");
    }

    @Test
    @DisplayName("메뉴가 존재하지 않으면, 메뉴 삭제 실패")
    void givenMenuNotExists_whenDeleteMenu_thenFail() {
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();

        memoryShopRepository.save(shop);

        Assertions.assertThrows(MenuNotExistsException.class, () -> {
            menuService.deleteById(1L);
        });
    }

    @Test
    @DisplayName("메뉴가 존재하면, 메뉴 삭제 성공")
    void givenMenu_whenDeleteMenu_thenSuccess() {
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황금올리브").build();

        memoryShopRepository.save(shop);
        memoryMenuRepository.save(menu);

        Assertions.assertDoesNotThrow(() -> {
            menuService.deleteById(1L);
        });
    }

    // OptionGroup CRUD Test
    @Test
    @DisplayName("옵션그룹 생성 시 메뉴가 존재하지 않으면, MenuNotExistsException")
    void whenRegisterOptionGroupWithWrongMenuId_thenThrowMenuNotExistsException() {
        OptionGroupSaveRequest saveRequest = OptionGroupSaveRequest.builder()
            .menuId(1L)
            .name("부분육 선택")
            .required(true)
            .maxNumOfSelect(1)
            .build();

        Assertions.assertThrows(MenuNotExistsException.class, () -> {
            menuService.registerOptionGroup(saveRequest);
        });
    }

    @Test
    @DisplayName("메뉴가 존재하면, 옵션그룹 생성 성공")
    void whenRegisterOptionGroup_thenSuccess() {
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황금올리브").build();
        memoryMenuRepository.save(menu);

        OptionGroupSaveRequest saveRequest = OptionGroupSaveRequest.builder()
            .menuId(1L)
            .name("부분육 선택")
            .required(true)
            .maxNumOfSelect(1)
            .build();

        Assertions.assertDoesNotThrow(() -> {
            menuService.registerOptionGroup(saveRequest);
        });
    }

    @Test
    @DisplayName("옵션그룹 수정 시 옵션그룹이 존재하지 않으면, OptionGroupNotExistsException")
    void whenUpdateNotExistsOptionGroup_thenThrowOptionGroupNotExistsException() {
        OptionGroupUpdateRequest updateRequest = OptionGroupUpdateRequest.builder()
            .optionGroupId(1L).name("음료 선택").build();

        Assertions.assertThrows(OptionGroupNotExistsException.class, () -> {
            menuService.updateOptionGroup(updateRequest);
        });
    }

    @Test
    @DisplayName("옵션그룹 수정을 성공하면, 조회 시 수정된 정보로 조회되어야 함")
    void whenUpdateOptionGroupSuccess_thenChangedOptionGroupInfo() {
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황금올리브").build();
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder()
            .menu(menu).name("부분육 선택").required(true).maxNumOfSelect(1).build();
        memoryMenuRepository.save(menu);
        memoryOptionGroupRepository.save(optionGroup);

        OptionGroupUpdateRequest updateRequest = OptionGroupUpdateRequest.builder()
            .optionGroupId(1L).name("음료 선택").required(false).maxNumOfSelect(5).build();

        Assertions.assertDoesNotThrow(() -> {
            menuService.updateOptionGroup(updateRequest);
        });

        OptionGroupJpaEntity result = memoryOptionGroupRepository.findById(1L).get();
        assertThat(result.getName()).isEqualTo("음료 선택");
        assertThat(result.isRequired()).isFalse();
        assertThat(result.getMaxNumOfSelect()).isEqualTo(5);
    }

    @Test
    @DisplayName("옵션그룹 삭제 시 옵션그룹이 존재하지 않으면, OptionGroupNotExistsException")
    void whenDeleteNotExistsOptionGroup_thenThrowOptionGroupNotExistsException() {
        Assertions.assertThrows(OptionGroupNotExistsException.class, () -> {
            menuService.deleteOptionGroup(1L);
        });
    }

    @Test
    @DisplayName("옵션그룹이 존재하면 삭제 성공")
    void whenDeleteOptionGroup_thenSuccess() {
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황금올리브").build();
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder()
            .menu(menu).name("부분육 선택").required(true).maxNumOfSelect(1).build();
        memoryMenuRepository.save(menu);
        memoryOptionGroupRepository.save(optionGroup);

        Assertions.assertDoesNotThrow(() -> {
            menuService.deleteOptionGroup(1L);
        });
    }

    // Option CRUD Test
    @Test
    @DisplayName("옵션 생성 시 옵션그룹이 존재하지 않으면, OptionGroupNotExistsException")
    void whenRegisterOptionWithWrongOptionGroupId_thenThrowOptionGroupNotExistsException() {
        OptionSaveRequest saveRequest = OptionSaveRequest.builder()
            .optionGroupId(1L).name("순살").build();

        Assertions.assertThrows(OptionGroupNotExistsException.class, () -> {
            menuService.registerOption(saveRequest);
        });
    }

    @Test
    @DisplayName("옵션그룹이 존재하면, 옵션 생성 성공")
    void whenRegisterOption_thenSuccess() {
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황금올리브").build();
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder()
            .menu(menu).name("부분육 선택").required(true).maxNumOfSelect(1).build();

        memoryMenuRepository.save(menu);
        memoryOptionGroupRepository.save(optionGroup);

        OptionSaveRequest saveRequest = OptionSaveRequest.builder()
            .optionGroupId(1L).name("순살").build();

        Assertions.assertDoesNotThrow(() -> {
            menuService.registerOption(saveRequest);
        });
    }

    @Test
    @DisplayName("옵션 수정 시 옵션이 존재하지 않으면, OptionNotExistsException")
    void whenUpdateNotExistsOption_thenThrowOptionNotExistsException() {
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황금올리브").build();
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder()
            .menu(menu).name("부분육 선택").required(true).maxNumOfSelect(1).build();

        memoryMenuRepository.save(menu);
        memoryOptionGroupRepository.save(optionGroup);

        OptionUpdateRequest updateRequest = OptionUpdateRequest.builder()
            .optionId(1L).name("닭다리").price(9999).build();

        Assertions.assertThrows(OptionNotExistsException.class, () -> {
            menuService.updateOption(updateRequest);
        });
    }

    @Test
    @DisplayName("옵션 수정을 성공하면, 조회 시 수정된 정보로 조회되어야 함")
    void whenUpdateOptionSuccess_thenChangedOptionInfo() {
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황금올리브").build();
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder()
            .menu(menu).name("부분육 선택").required(true).maxNumOfSelect(1).build();
        OptionJpaEntity option = OptionJpaEntity.builder()
            .optionGroup(optionGroup).name("순살").price(7000).build();

        memoryMenuRepository.save(menu);
        memoryOptionGroupRepository.save(optionGroup);
        memoryOptionRepository.save(option);

        OptionUpdateRequest updateRequest = OptionUpdateRequest.builder()
            .optionId(1L).name("닭다리").price(9999).build();

        Assertions.assertDoesNotThrow(() -> {
            menuService.updateOption(updateRequest);
        });

        OptionJpaEntity result = memoryOptionRepository.findById(1L).get();
        assertThat(result.getName()).isEqualTo("닭다리");
        assertThat(result.getPrice()).isEqualTo(9999);
    }

    @Test
    @DisplayName("옵션 삭제 시 옵션이 존재하지 않으면, OptionNotExistsException")
    void whenDeleteNotExistsOption_thenThrowOptionpNotExistsException() {
        Assertions.assertThrows(OptionNotExistsException.class, () -> {
            menuService.deleteOption(1L);
        });
    }

    @Test
    @DisplayName("옵션이 존재하면 삭제 성공")
    void whenDeleteOption_thenSuccess() {
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황금올리브").build();
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder()
            .menu(menu).name("부분육 선택").required(true).maxNumOfSelect(1).build();
        OptionJpaEntity option = OptionJpaEntity.builder()
            .optionGroup(optionGroup).name("순살").price(7000).build();

        memoryMenuRepository.save(menu);
        memoryOptionGroupRepository.save(optionGroup);
        memoryOptionRepository.save(option);

        Assertions.assertDoesNotThrow(() -> {
            menuService.deleteOption(1L);
        });
    }
}
