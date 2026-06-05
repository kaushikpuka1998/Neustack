package com.kgstrivers.neustack.ENTITIES;


import com.kgstrivers.neustack.REPOSITORIES.HasId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Order implements HasId {
    private String id;
    private String userId;
    private double totalAmount;
    private double discountAmount;
    private double finalAmount;
    private String discountCode;
    private Date createdDate;
    private List<OrderItem> items;
    public void addItem(OrderItem orderItem) {
        items.add(orderItem);
    }

    @Override
    public String getId() {
        return id;
    }
}
