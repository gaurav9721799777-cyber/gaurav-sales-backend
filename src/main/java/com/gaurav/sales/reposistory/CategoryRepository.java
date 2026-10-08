package com.gaurav.sales.reposistory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gaurav.sales.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findBySlug(String slug);

	boolean existsBySlug(String slug);

	boolean existsByNameIgnoreCase(String name);

	boolean existsBySlugAndIdNot(String slug, Long id);

	boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
