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
import com.oheat.food.dto.MenuGroupSaveRequest;
import com.oheat.food.dto.MenuGroupUpdateRequest;
import com.oheat.food.dto.MenuSaveToMenuGroupRequest;
import com.oheat.food.entity.MenuGroupJpaEntity;
import com.oheat.food.service.MenuGroupService;
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
public class MenuGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹 등록을 성공하면 Created 응답을 받는다.")
    void registerMenuGroup_shouldReturnCreated() throws Exception {
        MenuGroupSaveRequest saveReq = MenuGroupSaveRequest.builder()
            .shopId(1L)
            .name("황올")
            .build();
        String req = objectMapper.writeValueAsString(saveReq);

        doNothing()
            .when(menuGroupService)
            .registerMenuGroup(any());

        mockMvc.perform(post("/api/v1/foods/shops/menu-groups")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("shopId로 메뉴 그룹 조회하면, Ok 응답과 메뉴 그룹 정보 리스트를 받는다.")
    void findMenuGroupByShopId_shouldReturnOk() throws Exception {
        MenuGroupJpaEntity menuGroup = MenuGroupJpaEntity.builder()
            .name("인기메뉴")
            .build();

        when(menuGroupService.findMenuGroupByShopId(any()))
            .thenReturn((List.of(menuGroup)));

        mockMvc.perform(get("/api/v1/foods/shops/menu-groups/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("인기메뉴"));
    }

    @Test
    @DisplayName("메뉴 그룹 정보 수정을 성공하면 Ok 응답을 받는다.")
    void updateMenuGroup_shouldReturnOk() throws Exception {
        MenuGroupUpdateRequest updateReq = MenuGroupUpdateRequest.builder()
            .menuGroupId(1L)
            .name("후라이드")
            .build();
        String req = objectMapper.writeValueAsString(updateReq);

        doNothing()
            .when(menuGroupService)
            .updateMenuGroup(any());

        mockMvc.perform(put("/api/v1/foods/shops/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("메뉴 그룹 삭제에 성공하면 Ok 응답을 받는다.")
    void deleteMenuGroup_shouldReturnOk() throws Exception {
        doNothing()
            .when(menuGroupService)
            .deleteMenuGroup(any());

        mockMvc.perform(delete("/api/v1/foods/shops/menu-groups/1"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("메뉴 그룹에 메뉴 등록을 성공하면 Created 응답을 받는다.")
    void registerMenuToMenuGroup_shouldReturnCreated() throws Exception {
        MenuSaveToMenuGroupRequest saveReq = MenuSaveToMenuGroupRequest.builder()
            .menuGroupId(1L)
            .menuId(1L)
            .build();

        String req = objectMapper.writeValueAsString(saveReq);

        doNothing()
            .when(menuGroupService)
            .registerMenuToMenuGroup(any());

        mockMvc.perform(post("/api/v1/foods/shops/menu-groups/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("메뉴 그룹에서 메뉴 제거를 성공하면 Ok 응답을 받는다.")
    void deleteMenuFromMenuGroup_shouldReturnOk() throws Exception {
        doNothing()
            .when(menuGroupService)
            .deleteMenuFromMenuGroup(any());

        mockMvc.perform(delete("/api/v1/foods/shops/menu-groups/menu/1"))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
