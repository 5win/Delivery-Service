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
import com.oheat.food.dto.CategorySaveRequest;
import com.oheat.food.dto.CategoryUpdateRequest;
import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.service.CategoryService;
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
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리 등록에 성공하면 Created 응답을 받는다.")
    void registerCategory_shouldReturnCreated() throws Exception {
        CategorySaveRequest saveReq = CategorySaveRequest.builder()
            .name("치킨")
            .build();

        String req = objectMapper.writeValueAsString(saveReq);

        doNothing()
            .when(categoryService)
            .registerCategory(any());

        mockMvc.perform(post("/api/v1/foods/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("카테고리를 조회하면 Ok 응답과 카테고리 목록을 받는다.")
    void findAllCategory_shouldReturnOkAndCategoryList() throws Exception {
        CategoryJpaEntity category = CategoryJpaEntity.builder()
            .name("치킨")
            .build();

        when(categoryService.findAllCategory())
            .thenReturn(List.of(category));

        mockMvc.perform(get("/api/v1/foods/categories"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("치킨"));
    }

    @Test
    @DisplayName("카테고리 수정에 성공하면 Ok 응답을 받는다.")
    void updateCategory_shouldReturnOk() throws Exception {
        CategoryUpdateRequest updateReq = CategoryUpdateRequest.builder()
            .prevName("치킨")
            .newName("피자")
            .build();
        String req = objectMapper.writeValueAsString(updateReq);

        doNothing()
            .when(categoryService)
            .updateCategory(any());

        mockMvc.perform(put("/api/v1/foods/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카테고리 삭제에 성공하면 Ok 응답을 받는다.")
    void deleteCategory_shouldReturnOk() throws Exception {
        doNothing()
            .when(categoryService)
            .deleteCategory(any());

        mockMvc.perform(delete("/api/v1/foods/categories")
                .param("name", "치킨"))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
