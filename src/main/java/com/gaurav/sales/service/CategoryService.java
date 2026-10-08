package com.gaurav.sales.service;

import java.util.List;
import com.gaurav.sales.dtos.CategoryRequest;
import com.gaurav.sales.dtos.CategoryResponse;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);

    CategoryResponse getCategoryBySlug(String slug);

    CategoryResponse updateCategory(
            Long id,
            CategoryRequest request
    );

    void deleteCategory(Long id);

	CategoryResponse updateCategoryStatus(Long id, Boolean active);
}
