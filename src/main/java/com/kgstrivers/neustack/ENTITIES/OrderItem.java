package com.kgstrivers.neustack.ENTITIES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderItem {
    private String id; // Unique identifier for order item
    private String productId;
    private int quantity;
    private double price;

    public OrderItem(String productId, int quantity, double price) {
        this.id = UUID.randomUUID().toString();
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItem fromCartItem(CartItem cartItem) {
        return new OrderItem(cartItem.getId(), cartItem.getProductId(),
                cartItem.getQuantity(), cartItem.getPrice());
    }
}