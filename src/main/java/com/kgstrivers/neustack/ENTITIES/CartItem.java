package com.kgstrivers.neustack.ENTITIES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@RequiredArgsConstructor
public class CartItem {
    private String id; // New field
    private String cartId; // New field
    private String productId;
    private int quantity;
    private String name;
    private String description;
    private String userId;
    private double price; // New field
    private String imgUrl;

    public CartItem(Product product, int quantity, String userId,String cartId) {
        this.id = UUID.randomUUID().toString();
        this.productId = product.getId();  // Assuming productId is derived from the product
        this.quantity = quantity;
        this.name = product.getName();
        this.description = product.getDescription();
        this.userId = userId;
        this.cartId = cartId;
        this.imgUrl = product.getImgUrl();
        calculatePrice(quantity, product.getPrice());
    }

    public void calculatePrice(int quantity, double price){
        this.setPrice(quantity*price);
    }
}