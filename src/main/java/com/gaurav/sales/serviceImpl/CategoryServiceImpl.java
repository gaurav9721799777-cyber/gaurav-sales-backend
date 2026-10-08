package com.gaurav.sales.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaurav.sales.dtos.CategoryRequest;
import com.gaurav.sales.dtos.CategoryResponse;
import com.gaurav.sales.entity.Category;
import com.gaurav.sales.exceptions.DuplicateResourceException;
import com.gaurav.sales.exceptions.ResourceNotFoundException;
import com.gaurav.sales.reposistory.CategoryRepository;
import com.gaurav.sales.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;


    // CREATE CATEGORY
    @Override
    public CategoryResponse createCategory(
            CategoryRequest request) {

        // Check duplicate name
        if (categoryRepository.existsByNameIgnoreCase(
                request.getName())) {

            throw new DuplicateResourceException(
                    "Category already exists with name: "
                            + request.getName()
            );
        }


        // Check duplicate slug
        if (categoryRepository.existsBySlug(
                request.getSlug())) {

            throw new DuplicateResourceException(
                    "Category already exists with slug: "
                            + request.getSlug()
            );
        }


        Category category = new Category();

        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(request.getDescription());
        category.setActive(true);

        Category savedCategory =
                categoryRepository.save(category);

        return mapToResponse(savedCategory);
    }


    // GET ALL CATEGORIES
    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // GET CATEGORY BY ID
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {

        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(category);
    }


    // GET CATEGORY BY SLUG
    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryBySlug(
            String slug) {

        Category category =
                categoryRepository.findBySlug(slug)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with slug: "
                                                + slug
                                )
                        );

        return mapToResponse(category);
    }


    // UPDATE CATEGORY
    @Override
    public CategoryResponse updateCategory(
            Long id,
            CategoryRequest request) {

        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: "
                                                + id
                                )
                        );


        // Check duplicate name
        if (categoryRepository
                .existsByNameIgnoreCaseAndIdNot(
                        request.getName(),
                        id
                )) {

            throw new DuplicateResourceException(
                    "Another category already exists with name: "
                            + request.getName()
            );
        }


        // Check duplicate slug
        if (categoryRepository
                .existsBySlugAndIdNot(
                        request.getSlug(),
                        id
                )) {

            throw new DuplicateResourceException(
                    "Another category already exists with slug: "
                            + request.getSlug()
            );
        }


        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setDescription(
                request.getDescription()
        );


        Category updatedCategory =
                categoryRepository.save(category);

        return mapToResponse(updatedCategory);
    }


    // DELETE CATEGORY
    @Override
    public void deleteCategory(Long id) {

        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found with id: "
                                                + id
                                )
                        );


        /*
         * Recommended for ecommerce:
         * Don't permanently delete the category.
         * Deactivate it instead.
         */

        category.setActive(false);

        categoryRepository.save(category);
    }

    @Override
    public CategoryResponse updateCategoryStatus(Long id, Boolean active) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found with id: " + id));

        category.setActive(active);

        Category savedCategory = categoryRepository.save(category);

        return mapToResponse(savedCategory);
    }

    // ENTITY -> RESPONSE DTO
    private CategoryResponse mapToResponse(
            Category category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .active(category.getActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}