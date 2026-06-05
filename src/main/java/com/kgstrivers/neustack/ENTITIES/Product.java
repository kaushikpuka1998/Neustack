package com.kgstrivers.neustack.ENTITIES;

// ... existing code ...

import com.kgstrivers.neustack.REPOSITORIES.HasId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
public class Product implements HasId {
    private String id;
    private String imgUrl;
    private String description;
    private String name;
    private double price;
    private int stock;

    public Product(String name, double price, int stock) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    @Override
    public String getId() {
        return id;
    }
}
