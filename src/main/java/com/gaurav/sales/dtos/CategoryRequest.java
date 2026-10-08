package com.gaurav.sales.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(
        min = 2,
        max = 100,
        message = "Category name must be between 2 and 100 characters"
    )
    private String name;

    @NotBlank(message = "Category slug is required")
    @Size(
        min = 2,
        max = 150,
        message = "Category slug must be between 2 and 150 characters"
    )
    private String slug;

    @Size(
        max = 500,
        message = "Description cannot exceed 500 characters"
    )
    private String description;
}
