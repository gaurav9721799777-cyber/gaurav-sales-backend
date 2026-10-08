package com.gaurav.sales.controller;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gaurav.sales.dtos.CategoryRequest;
import com.gaurav.sales.dtos.CategoryResponse;
import com.gaurav.sales.exceptions.ApiResponse;
import com.gaurav.sales.service.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;

    // CREATE CATEGORY
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse category =
                categoryService.createCategory(request);

        ApiResponse<CategoryResponse> response =
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category created successfully")
                        .data(category)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET ALL CATEGORIES
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>>
    getAllCategories() {

        List<CategoryResponse> categories =
                categoryService.getAllCategories();

        ApiResponse<List<CategoryResponse>> response =
                ApiResponse.<List<CategoryResponse>>builder()
                        .success(true)
                        .message("Categories fetched successfully")
                        .data(categories)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // GET CATEGORY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>>
    getCategoryById(@PathVariable Long id) {

        CategoryResponse category =
                categoryService.getCategoryById(id);

        ApiResponse<CategoryResponse> response =
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category fetched successfully")
                        .data(category)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // GET CATEGORY BY SLUG
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<CategoryResponse>>
    getCategoryBySlug(@PathVariable String slug) {

        CategoryResponse category =
                categoryService.getCategoryBySlug(slug);

        ApiResponse<CategoryResponse> response =
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category fetched successfully")
                        .data(category)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // UPDATE CATEGORY
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>>
    updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        CategoryResponse category =
                categoryService.updateCategory(id, request);

        ApiResponse<CategoryResponse> response =
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category updated successfully")
                        .data(category)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    // DELETE / DEACTIVATE CATEGORY
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>>
    deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);

        ApiResponse<Void> response =
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Category deleted successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategoryStatus(
            @PathVariable Long id,
            @RequestParam Boolean active) {

        CategoryResponse category =
                categoryService.updateCategoryStatus(id, active);

        ApiResponse<CategoryResponse> response =
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message(active
                                ? "Category activated successfully"
                                : "Category deactivated successfully")
                        .data(category)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }
}
