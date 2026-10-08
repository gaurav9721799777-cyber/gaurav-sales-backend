package com.gaurav.sales.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BrandRequest {

    @NotBlank(message = "Brand name is required")
    @Size(
        min = 2,
        max = 100,
        message = "Brand name must be between 2 and 100 characters"
    )
    private String name;

    @NotBlank(message = "Brand slug is required")
    @Size(
        min = 2,
        max = 150,
        message = "Brand slug must be between 2 and 150 characters"
    )
    private String slug;

    private String logoUrl;
    
    private Boolean active;
}