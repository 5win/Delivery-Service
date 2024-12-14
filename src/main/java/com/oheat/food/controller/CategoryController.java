package com.oheat.food.controller;

import com.oheat.food.dto.CategoryFindAllResponse;
import com.oheat.food.dto.CategorySaveRequest;
import com.oheat.food.dto.CategoryUpdateRequest;
import com.oheat.food.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/food/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> registerCategory(@RequestBody CategorySaveRequest saveRequest) {
        System.out.println(saveRequest.getName());
        categoryService.registerCategory(saveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public List<CategoryFindAllResponse> findAll() {
        return categoryService.findAllCategory().stream()
            .map(CategoryFindAllResponse::from)
            .toList();
    }

    @PutMapping
    public ResponseEntity<?> updateCategory(@RequestBody CategoryUpdateRequest updateRequest) {
        categoryService.updateCategory(updateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCategory(@RequestParam String name) {
        categoryService.deleteCategory(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
