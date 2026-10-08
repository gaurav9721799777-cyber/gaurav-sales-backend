package com.gaurav.sales.reposistory;

import com.gaurav.sales.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByCartId(String cartId);
	boolean existsByCartId(String cartId);
}