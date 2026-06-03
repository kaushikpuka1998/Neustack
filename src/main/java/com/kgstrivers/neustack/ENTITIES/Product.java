package com.kgstrivers.neustack.ENTITIES;

// ... existing code ...

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Product {
    private String id;
    private String name;
    private double price;
    private int stock;
}
