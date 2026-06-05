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
    private String email;
    private String password;
    private Cart cart;
    private List<Order> orders; // List to hold multiple orders


    public User(String name, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
        this.cart = new Cart();
        this.orders = new ArrayList<>();
    }

    @Override
    public String getId() {
        return id;
    }
}
