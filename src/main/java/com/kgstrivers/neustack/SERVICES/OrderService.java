package com.kgstrivers.neustack.SERVICES;

import com.kgstrivers.neustack.ENTITIES.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final CartService cartService;

    public OrderService(CartService cartService) {
        this.cartService = cartService;
    }

    public Order createOrder(String userId, String discountCode) {
        // Check out the cart to get total amount and order items
        return cartService.checkout(userId, discountCode);
    }
}
