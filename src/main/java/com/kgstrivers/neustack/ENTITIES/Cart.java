package com.kgstrivers.neustack.ENTITIES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Cart {
    private String id;
    private String userId;
    private String status; // ACTIVE/CHECKED_OUT
    private long createdAt;
    private double price;
    private List<CartItem> cartItems;
}
