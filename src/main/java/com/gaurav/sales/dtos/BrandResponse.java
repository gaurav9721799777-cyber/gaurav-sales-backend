package com.gaurav.sales.dtos;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BrandResponse {

    private Long id;

    private String name;

    private String slug;

    private String logoUrl;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
