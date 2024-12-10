package com.paccy.repository;

import com.paccy.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    public  Cart findByCustomerId(long UserId);
}
