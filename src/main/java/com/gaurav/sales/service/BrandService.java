package com.gaurav.sales.service;


import java.util.List;

import com.gaurav.sales.dtos.BrandRequest;
import com.gaurav.sales.dtos.BrandResponse;

public interface BrandService {

    BrandResponse createBrand(BrandRequest request);

    List<BrandResponse> getAllBrands();

    BrandResponse getBrandById(Long id);

    BrandResponse getBrandBySlug(String slug);

    BrandResponse updateBrand(Long id, BrandRequest request);

    void deleteBrand(Long id);
}
