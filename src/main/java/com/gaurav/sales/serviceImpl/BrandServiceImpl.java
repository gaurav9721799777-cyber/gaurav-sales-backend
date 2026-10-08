package com.gaurav.sales.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gaurav.sales.dtos.BrandRequest;
import com.gaurav.sales.dtos.BrandResponse;
import com.gaurav.sales.entity.Brand;
import com.gaurav.sales.exceptions.DuplicateResourceException;
import com.gaurav.sales.exceptions.ResourceNotFoundException;
import com.gaurav.sales.reposistory.BrandRepository;
import com.gaurav.sales.service.BrandService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandServiceImpl implements BrandService {

	private final BrandRepository brandRepository;

	// CREATE BRAND
	@Override
	public BrandResponse createBrand(BrandRequest request) {

		// Check duplicate name
		if (brandRepository.existsByNameIgnoreCase(request.getName())) {
			throw new DuplicateResourceException("Brand already exists with name: " + request.getName());
		}

		// Check duplicate slug
		if (brandRepository.existsBySlug(request.getSlug())) {
			throw new DuplicateResourceException("Brand already exists with slug: " + request.getSlug());
		}

		Brand brand = new Brand();

		brand.setName(request.getName());
		brand.setSlug(request.getSlug());
		brand.setLogoUrl(request.getLogoUrl());
		brand.setActive(true);

		Brand savedBrand = brandRepository.save(brand);

		return mapToResponse(savedBrand);
	}

	// GET ALL BRANDS
	@Override
	@Transactional(readOnly = true)
	public List<BrandResponse> getAllBrands() {

		return brandRepository.findAll().stream().map(this::mapToResponse).toList();
	}

	// GET BRAND BY ID
	@Override
	@Transactional(readOnly = true)
	public BrandResponse getBrandById(Long id) {

		Brand brand = brandRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

		return mapToResponse(brand);
	}

	// GET BRAND BY SLUG
	@Override
	@Transactional(readOnly = true)
	public BrandResponse getBrandBySlug(String slug) {

		Brand brand = brandRepository.findBySlug(slug)
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found with slug: " + slug));

		return mapToResponse(brand);
	}

	// UPDATE BRAND
	@Override
	public BrandResponse updateBrand(Long id, BrandRequest request) {

		Brand brand = brandRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

		// Check duplicate name
		if (brandRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {

			throw new DuplicateResourceException("Another brand already exists with name: " + request.getName());
		}

		// Check duplicate slug
		if (brandRepository.existsBySlugAndIdNot(request.getSlug(), id)) {

			throw new DuplicateResourceException("Another brand already exists with slug: " + request.getSlug());
		}

		brand.setName(request.getName());
		brand.setSlug(request.getSlug());
		brand.setLogoUrl(request.getLogoUrl());

		Brand updatedBrand = brandRepository.save(brand);

		return mapToResponse(updatedBrand);
	}

	// DELETE BRAND
	@Override
	public void deleteBrand(Long id) {

		Brand brand = brandRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + id));

		/*
		 * Recommended: Don't physically delete brands in ecommerce. Instead deactivate
		 * them.
		 */

		brand.setActive(false);

		brandRepository.save(brand);
	}

	// ENTITY -> RESPONSE DTO
	private BrandResponse mapToResponse(Brand brand) {
		return BrandResponse.builder().id(brand.getId()).name(brand.getName()).slug(brand.getSlug())
				.logoUrl(brand.getLogoUrl()).active(brand.getActive()).createdAt(brand.getCreatedAt())
				.updatedAt(brand.getUpdatedAt()).build();
	}
}