package com.gaurav.sales.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gaurav.sales.dtos.BrandRequest;
import com.gaurav.sales.dtos.BrandResponse;
import com.gaurav.sales.exceptions.ApiResponse;
import com.gaurav.sales.service.BrandService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BrandController {

	private final BrandService brandService;

	// CREATE BRAND
	@PostMapping
	public ResponseEntity<ApiResponse<BrandResponse>> createBrand(@Valid @RequestBody BrandRequest request) {

		BrandResponse brand = brandService.createBrand(request);

		ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>builder().success(true)
				.message("Brand created successfully").data(brand).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// GET ALL BRANDS
	@GetMapping
	public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllBrands() {

		List<BrandResponse> brands = brandService.getAllBrands();

		ApiResponse<List<BrandResponse>> response = ApiResponse.<List<BrandResponse>>builder().success(true)
				.message("Brands fetched successfully").data(brands).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	// GET BRAND BY ID
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<BrandResponse>> getBrandById(@PathVariable Long id) {

		BrandResponse brand = brandService.getBrandById(id);

		ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>builder().success(true)
				.message("Brand fetched successfully").data(brand).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	// GET BRAND BY SLUG
	@GetMapping("/slug/{slug}")
	public ResponseEntity<ApiResponse<BrandResponse>> getBrandBySlug(@PathVariable String slug) {

		BrandResponse brand = brandService.getBrandBySlug(slug);

		ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>builder().success(true)
				.message("Brand fetched successfully").data(brand).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	// UPDATE BRAND
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(@PathVariable Long id,
			@Valid @RequestBody BrandRequest request) {

		BrandResponse brand = brandService.updateBrand(id, request);

		ApiResponse<BrandResponse> response = ApiResponse.<BrandResponse>builder().success(true)
				.message("Brand updated successfully").data(brand).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}

	// DELETE / DEACTIVATE BRAND
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteBrand(@PathVariable Long id) {

		brandService.deleteBrand(id);

		ApiResponse<Void> response = ApiResponse.<Void>builder().success(true).message("Brand deleted successfully")
				.data(null).timestamp(LocalDateTime.now()).build();

		return ResponseEntity.ok(response);
	}
}