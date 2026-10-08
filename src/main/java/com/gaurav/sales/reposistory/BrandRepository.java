package com.gaurav.sales.reposistory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gaurav.sales.entity.Brand;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

	Optional<Brand> findBySlug(String slug);

	boolean existsBySlug(String slug);

	boolean existsByNameIgnoreCase(String name);

	boolean existsBySlugAndIdNot(String slug, Long id);

	boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}