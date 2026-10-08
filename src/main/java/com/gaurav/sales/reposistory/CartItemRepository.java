package com.gaurav.sales.reposistory;

import com.gaurav.sales.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository
        extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCart_CartIdAndProduct_Id(
            String cartId,
            Long productId
    );


    List<CartItem> findAllByCart_CartId(
            String cartId
    );


    void deleteByCart_CartIdAndProduct_Id(
            String cartId,
            Long productId
    );


    void deleteAllByCart_CartId(
            String cartId
    );
}