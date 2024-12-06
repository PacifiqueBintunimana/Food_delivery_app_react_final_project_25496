package com.paccy.repository;

import com.paccy.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
