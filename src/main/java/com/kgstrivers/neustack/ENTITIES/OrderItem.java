package com.kgstrivers.neustack.ENTITIES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderItem {
    private String id; // Unique identifier for order item
    private String productId;
    private String imgUrl;
    private String name;
    private int quantity;
    private double price;
    private Date creationDate;

    public OrderItem(String productId, int quantity, double price, String name, String imgUrl) {
        this.id = UUID.randomUUID().toString();
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.imgUrl = imgUrl;
    }
}