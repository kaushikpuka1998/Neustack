package com.kgstrivers.neustack.ENTITIES;

import com.kgstrivers.neustack.REPOSITORIES.HasId;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class User implements HasId {
    private String id;
    private String name;
    private Cart cart;
    private List<Order> orders; // List to hold multiple orders


    public User(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.cart = new Cart();
        this.orders = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id;
    }
}
