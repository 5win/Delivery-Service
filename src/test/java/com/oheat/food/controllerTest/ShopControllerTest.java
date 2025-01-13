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
import com.oheat.food.dto.ShopFindResponse;
import com.oheat.food.dto.ShopSaveRequest;
import com.oheat.food.dto.ShopUpdateRequest;
import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.service.ShopService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ShopService shopService;

    @DisplayName("매장 생성을 성공하고 Created 응답을 받는다")
    @Test
    void registerShop_shouldReturnCreated() throws Exception {
        ShopSaveRequest saveReq = ShopSaveRequest.builder()
            .name("bbq")
            .category("치킨")
            .build();
        String req = objectMapper.writeValueAsString(saveReq);

        doNothing()
            .when(shopService)
            .registerShop(any());

        this.mockMvc.perform(post("/api/v1/foods/shops")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @DisplayName("카테고리 기준으로 매장을 조회하면, Ok 응답과 매장정보를 받는다.")
    @Test
    void findShopByCategory_shouldReturnOkAndShopPage() throws Exception {
        CategoryJpaEntity category = CategoryJpaEntity.builder()
            .name("치킨")
            .build();
        ShopJpaEntity shop = ShopJpaEntity.builder()
            .category(category)
            .name("bbq")
            .build();

        when(shopService.findShopByCategory(any(), any()))
            .thenReturn(new PageImpl<>(List.of(ShopFindResponse.from(shop)), Pageable.ofSize(10), 1));

        this.mockMvc.perform(get("/api/v1/foods/shops")
                .param("category", "치킨"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("bbq"))
            .andExpect(jsonPath("$.content[0].category").value("치킨"));
    }

    @DisplayName("매장 정보 수정을 성공하면, Ok 응답을 받는다.")
    @Test
    void updateShop_shouldReturnOk() throws Exception {
        ShopUpdateRequest updateReq = ShopUpdateRequest.builder()
            .category("치킨")
            .name("BHC")
            .build();

        String req = objectMapper.writeValueAsString(updateReq);

        this.mockMvc.perform(put("/api/v1/foods/shops")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("매장 삭제를 성공하면, Ok 응답을 받는다.")
    @Test
    void deleteShop_shouldReturnOk() throws Exception {
        doNothing()
            .when(shopService)
            .deleteShop(1L);

        this.mockMvc.perform(delete("/api/v1/foods/shops/1"))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
