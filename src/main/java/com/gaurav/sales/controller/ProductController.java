package com.gaurav.sales.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gaurav.sales.dtos.ProductRequest;
import com.gaurav.sales.dtos.ProductResponse;
import com.gaurav.sales.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ProductController {

	private final ProductService productService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductResponse> createProduct(@Valid @ModelAttribute ProductRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
	}

	@GetMapping
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
		return ResponseEntity.ok(productService.getAllProducts());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
		return ResponseEntity.ok(productService.getProductById(id));
	}

	@GetMapping("/slug/{slug}")
	public ResponseEntity<ProductResponse> getProductBySlug(@PathVariable String slug) {
		return ResponseEntity.ok(productService.getProductBySlug(slug));
	}

	@GetMapping("/category/{categorySlug}")
	public ResponseEntity<List<ProductResponse>> getByCategory(@PathVariable String categorySlug) {
		return ResponseEntity.ok(productService.getProductsByCategory(categorySlug));
	}

	@GetMapping("/brand/{brandSlug}")
	public ResponseEntity<List<ProductResponse>> getByBrand(@PathVariable String brandSlug) {
		return ResponseEntity.ok(productService.getProductsByBrand(brandSlug));
	}

	@GetMapping("/category/{categorySlug}/brand/{brandSlug}")
	public ResponseEntity<List<ProductResponse>> getByCategoryAndBrand(@PathVariable String categorySlug,
			@PathVariable String brandSlug) {
		return ResponseEntity.ok(productService.getProductsByCategoryAndBrand(categorySlug, brandSlug));
	}

	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
			@Valid @ModelAttribute ProductRequest request) {

		return ResponseEntity.ok(productService.updateProduct(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {
		productService.deactivateProduct(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/{id}/activate")
	public ResponseEntity<Void> activateProduct(@PathVariable Long id) {
		productService.activateProduct(id);
		return ResponseEntity.noContent().build();
	}
}