package com.kgstrivers.neustack.ENTITIES;

import com.kgstrivers.neustack.REPOSITORIES.HasId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class DiscountCode implements HasId {
    private String id;
    private String code;
    private double xPercentage;
    private int everyNthOrder;

    public DiscountCode(int everyNthOrder, double percentage) {
        this.id = UUID.randomUUID().toString();
        this.code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.xPercentage = percentage;
        this.everyNthOrder = everyNthOrder;
    }

    @Override
    public String getId() {
        return id;
    }
}
