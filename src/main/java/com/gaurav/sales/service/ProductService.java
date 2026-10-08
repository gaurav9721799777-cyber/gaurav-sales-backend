package com.gaurav.sales.service;

import java.util.List;

import com.gaurav.sales.dtos.ProductRequest;
import com.gaurav.sales.dtos.ProductResponse;

public interface ProductService {

	ProductResponse createProduct(ProductRequest request);

	List<ProductResponse> getAllProducts();

	ProductResponse getProductById(Long id);

	ProductResponse getProductBySlug(String slug);

	List<ProductResponse> getProductsByCategory(String categorySlug);

	List<ProductResponse> getProductsByBrand(String brandSlug);

	List<ProductResponse> getProductsByCategoryAndBrand(String categorySlug, String brandSlug);

	ProductResponse updateProduct(Long id, ProductRequest request);

	void deactivateProduct(Long id);

	void activateProduct(Long id);
}