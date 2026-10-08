package com.gaurav.sales.reposistory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gaurav.sales.entity.Product;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findBySlugAndActiveTrue(String slug);

    List<Product> findAllByActiveTrue();

    List<Product> findByCategory_SlugAndActiveTrue(
            String categorySlug
    );

    List<Product> findByBrand_SlugAndActiveTrue(
            String brandSlug
    );

    List<Product> findByCategory_SlugAndBrand_SlugAndActiveTrue(
            String categorySlug,
            String brandSlug
    );

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(
            String slug,
            Long id
    );

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(
            String sku,
            Long id
    );
}