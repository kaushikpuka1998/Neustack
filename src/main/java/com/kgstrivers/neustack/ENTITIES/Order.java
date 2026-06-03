package com.kgstrivers.neustack.ENTITIES;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Order {
    private String id;
    private String userId;
    private double totalAmount;
    private int discountAmount;
    private double finalAmount;
    private String discountCode;
    private List<OrderItem> items;


    public void addItem(OrderItem orderItem) {
        items.add(orderItem);
    }
}
