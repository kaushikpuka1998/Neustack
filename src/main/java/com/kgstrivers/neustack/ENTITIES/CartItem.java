package com.kgstrivers.neustack.ENTITIES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CartItem {
    private String id; // New field
    private String cartId; // New field
    private String productId;
    private int quantity;
    private String userId;
    private double price; // New field
}