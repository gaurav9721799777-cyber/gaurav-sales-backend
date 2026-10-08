package com.gaurav.sales.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaurav.sales.dtos.ProductRequest;
import com.gaurav.sales.dtos.ProductResponse;
import com.gaurav.sales.entity.Brand;
import com.gaurav.sales.entity.Category;
import com.gaurav.sales.entity.Product;
import com.gaurav.sales.exceptions.ResourceNotFoundException;
import com.gaurav.sales.reposistory.BrandRepository;
import com.gaurav.sales.reposistory.CategoryRepository;
import com.gaurav.sales.reposistory.ProductRepository;
import com.gaurav.sales.service.FileStorageService;
import com.gaurav.sales.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	private final BrandRepository brandRepository;

	private final CategoryRepository categoryRepository;

	private final FileStorageService fileStorageService;

	// =========================================================
	// CREATE PRODUCT
	// =========================================================

	@Override
	public ProductResponse createProduct(ProductRequest request) {

		// ---------------------------------------------
		// Check slug
		// ---------------------------------------------

		if (productRepository.existsBySlug(request.getSlug())) {

			throw new ResourceNotFoundException("Product slug already exists");
		}

		// ---------------------------------------------
		// Check SKU
		// ---------------------------------------------

		if (request.getSku() != null && !request.getSku().isBlank()
				&& productRepository.existsBySku(request.getSku())) {

			throw new ResourceNotFoundException("Product SKU already exists");
		}

		// ---------------------------------------------
		// Find Brand
		// ---------------------------------------------

		Brand brand = brandRepository.findById(request.getBrandId())
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + request.getBrandId()));

		// ---------------------------------------------
		// Find Category
		// ---------------------------------------------

		Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
				() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

		// ---------------------------------------------
		// Create Product
		// ---------------------------------------------

		String imageUrl = fileStorageService.saveProductImage(request.getImage());

		Product product = Product.builder().name(request.getName()).slug(request.getSlug())
				.description(request.getDescription()).price(request.getPrice())
				.discountPrice(request.getDiscountPrice()).stockQuantity(request.getStockQuantity())
				.sku(request.getSku()).imageUrl(imageUrl).brand(brand).category(category).active(true).build();

		Product savedProduct = productRepository.save(product);

		return mapToResponse(savedProduct);
	}

	// =========================================================
	// GET ALL ACTIVE PRODUCTS
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<ProductResponse> getAllProducts() {

		return productRepository.findAllByActiveTrue().stream().map(this::mapToResponse).toList();
	}

	// =========================================================
	// GET PRODUCT BY ID
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public ProductResponse getProductById(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		return mapToResponse(product);
	}

	// =========================================================
	// GET PRODUCT BY SLUG
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public ProductResponse getProductBySlug(String slug) {

		Product product = productRepository.findBySlugAndActiveTrue(slug)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));

		return mapToResponse(product);
	}

	// =========================================================
	// GET BY CATEGORY
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<ProductResponse> getProductsByCategory(String categorySlug) {

		return productRepository.findByCategory_SlugAndActiveTrue(categorySlug).stream().map(this::mapToResponse)
				.toList();
	}

	// =========================================================
	// GET BY BRAND
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<ProductResponse> getProductsByBrand(String brandSlug) {

		return productRepository.findByBrand_SlugAndActiveTrue(brandSlug).stream().map(this::mapToResponse).toList();
	}

	// =========================================================
	// GET BY CATEGORY + BRAND
	// =========================================================

	@Override
	@Transactional(readOnly = true)
	public List<ProductResponse> getProductsByCategoryAndBrand(String categorySlug, String brandSlug) {

		return productRepository.findByCategory_SlugAndBrand_SlugAndActiveTrue(categorySlug, brandSlug).stream()
				.map(this::mapToResponse).toList();
	}

	// =========================================================
	// UPDATE PRODUCT
	// =========================================================

	@Override
	public ProductResponse updateProduct(Long id, ProductRequest request) {

		// 1. Find existing product
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		// 2. Check slug
		if (productRepository.existsBySlugAndIdNot(request.getSlug(), id)) {

			throw new IllegalArgumentException("Product slug already exists");
		}

		// 3. Check SKU
		if (request.getSku() != null && !request.getSku().isBlank()
				&& productRepository.existsBySkuAndIdNot(request.getSku(), id)) {

			throw new IllegalArgumentException("Product SKU already exists");
		}

		// 4. Find brand
		Brand brand = brandRepository.findById(request.getBrandId())
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + request.getBrandId()));

		// 5. Find category
		Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
				() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

		// 6. Update normal product fields

		product.setName(request.getName());

		product.setSlug(request.getSlug());

		product.setDescription(request.getDescription());

		product.setPrice(request.getPrice());

		product.setDiscountPrice(request.getDiscountPrice());

		product.setStockQuantity(request.getStockQuantity());

		product.setSku(request.getSku());

		product.setBrand(brand);

		product.setCategory(category);

		// =====================================================
		// 7. UPDATE IMAGE
		// =====================================================

		if (request.getImage() != null && !request.getImage().isEmpty()) {

			// Existing image
			String oldImage = product.getImageUrl();

			// Save new image
			String newImage = fileStorageService.saveProductImage(request.getImage());

			// Set new image URL
			product.setImageUrl(newImage);

			// Delete old image
			if (oldImage != null && !oldImage.isBlank()) {

				fileStorageService.deleteProductImage(oldImage);
			}
		}

		// =====================================================
		// 8. SAVE PRODUCT
		// =====================================================

		Product updatedProduct = productRepository.save(product);

		// =====================================================
		// 9. RETURN RESPONSE
		// =====================================================

		return mapToResponse(updatedProduct);
	}

	// =========================================================
	// DEACTIVATE PRODUCT
	// =========================================================

	@Override
	public void deactivateProduct(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		if (!Boolean.TRUE.equals(product.getActive())) {

			throw new IllegalStateException("Product is already inactive");
		}

		product.setActive(false);

		productRepository.save(product);
	}

	// =========================================================
	// ACTIVATE PRODUCT
	// =========================================================

	@Override
	public void activateProduct(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

		if (Boolean.TRUE.equals(product.getActive())) {

			throw new IllegalStateException("Product is already active");
		}

		product.setActive(true);

		productRepository.save(product);
	}

	// =========================================================
	// MAPPER
	// =========================================================

	private ProductResponse mapToResponse(Product product) {

		return ProductResponse.builder().id(product.getId()).name(product.getName()).slug(product.getSlug())
				.description(product.getDescription()).price(product.getPrice())
				.discountPrice(product.getDiscountPrice()).stock(product.getStockQuantity()).sku(product.getSku())
				.mainImageUrl(product.getImageUrl()).active(product.getActive()).brandId(product.getBrand().getId())
				.brandName(product.getBrand().getName()).categoryId(product.getCategory().getId())
				.categoryName(product.getCategory().getName()).createdAt(product.getCreatedAt())
				.updatedAt(product.getUpdatedAt()).build();
	}
}