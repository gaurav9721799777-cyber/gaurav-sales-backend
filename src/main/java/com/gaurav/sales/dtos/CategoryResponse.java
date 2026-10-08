package com.gaurav.sales.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CategoryResponse {

    private Long id;

    private String name;

    private String slug;

    private String description;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
