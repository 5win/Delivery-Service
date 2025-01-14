package com.oheat.food.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oheat.food.dto.MenuSaveRequest;
import com.oheat.food.dto.MenuUpdateRequest;
import com.oheat.food.dto.OptionGroupSaveRequest;
import com.oheat.food.dto.OptionGroupUpdateRequest;
import com.oheat.food.dto.OptionSaveRequest;
import com.oheat.food.dto.OptionUpdateRequest;
import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.service.MenuService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MenuService menuService;

    // Menu Tests
    @Test
    @DisplayName("메뉴 등록에 성공하면 Created 응답을 받는다.")
    void registerMenu_shouldReturnCreated() throws Exception {
        MenuSaveRequest saveReq = MenuSaveRequest.builder()
            .name("황올")
            .shopId(1L)
            .price(20_000)
            .build();
        String req = objectMapper.writeValueAsString(saveReq);

        doNothing()
            .when(menuService)
            .registerMenu(any());

        mockMvc.perform(post("/api/v1/foods/shops/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("shopId로 메뉴를 조회하면 Ok 응답과 함께 메뉴 리스트를 받는다.")
    void findMenuByShopId_shouldReturnOkAndMenuList() throws Exception {
        ShopJpaEntity shop = ShopJpaEntity.builder()
            .name("bbq")
            .build();
        MenuJpaEntity menu = MenuJpaEntity.builder()
            .shop(shop)
            .name("황올")
            .price(20_000)
            .build();
        when(menuService.findByShopId(any()))
            .thenReturn(List.of(menu));

        mockMvc.perform(get("/api/v1/foods/shops/menus/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("황올"))
            .andExpect(jsonPath("$[0].price").value(20_000));
    }

    @Test
    @DisplayName("메뉴 정보 업데이트를 성공하면 Ok 응답을 받는다.")
    void updateMenu_shouldReturnOk() throws Exception {
        MenuUpdateRequest updateReq = MenuUpdateRequest.builder()
            .menuId(1L)
            .name("양념치킨")
            .price(10_000)
            .build();
        String req = objectMapper.writeValueAsString(updateReq);

        doNothing()
            .when(menuService)
            .updateMenu(any());

        mockMvc.perform(put("/api/v1/foods/shops/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("메뉴 삭제를 성공하면 Ok 응답을 받는다.")
    void deleteMenu_shouldReturnOk() throws Exception {
        doNothing()
            .when(menuService)
            .deleteById(any());

        mockMvc.perform(delete("/api/v1/foods/shops/menus/1"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    // Option Group Tests
    @Test
    @DisplayName("옵션 그룹 등록에 성공하면 Created 응답을 받는다.")
    void registerOptionGroup_shouldReturnCreated() throws Exception {
        OptionGroupSaveRequest saveReq = OptionGroupSaveRequest.builder()
            .menuId(1L)
            .name("음료")
            .build();
        String req = objectMapper.writeValueAsString(saveReq);

        doNothing()
            .when(menuService)
            .registerOptionGroup(any());

        mockMvc.perform(post("/api/v1/foods/shops/menus/options/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("옵션 그룹 정보 업데이트를 성공하면 Ok 응답을 받는다.")
    void updateOptionGroup_shouldReturnOk() throws Exception {
        OptionGroupUpdateRequest updateReq = OptionGroupUpdateRequest.builder()
            .optionGroupId(1L)
            .name("부분육 선택")
            .build();
        String req = objectMapper.writeValueAsString(updateReq);

        doNothing()
            .when(menuService)
            .updateOptionGroup(any());

        mockMvc.perform(put("/api/v1/foods/shops/menus/options/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("옵션 그룹 삭제를 성공하면 Ok 응답을 받는다.")
    void deleteOptionGroup_shouldReturnOk() throws Exception {
        doNothing()
            .when(menuService)
            .deleteOptionGroup(any());

        mockMvc.perform(delete("/api/v1/foods/shops/menus/options/groups/1"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    // Option Tests
    @Test
    @DisplayName("옵션 등록에 성공하면 Created 응답을 받는다.")
    void registerOption_shouldReturnCreated() throws Exception {
        OptionSaveRequest saveReq = OptionSaveRequest.builder()
            .optionGroupId(1L)
            .name("콜라")
            .price(2000)
            .build();
        String req = objectMapper.writeValueAsString(saveReq);

        doNothing()
            .when(menuService)
            .registerOption(any());

        mockMvc.perform(post("/api/v1/foods/shops/menus/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("옵션 정보 업데이트를 성공하면 Ok 응답을 받는다.")
    void updateOption_shouldReturnOk() throws Exception {
        OptionUpdateRequest updateReq = OptionUpdateRequest.builder()
            .optionId(1L)
            .name("사이다")
            .price(2000)
            .build();
        String req = objectMapper.writeValueAsString(updateReq);

        doNothing()
            .when(menuService)
            .updateOption(any());

        mockMvc.perform(put("/api/v1/foods/shops/menus/options")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("옵션 삭제를 성공하면 Ok 응답을 받는다.")
    void deleteOption_shouldReturnOk() throws Exception {
        doNothing()
            .when(menuService)
            .deleteOption(any());

        mockMvc.perform(delete("/api/v1/foods/shops/menus/options/1"))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
