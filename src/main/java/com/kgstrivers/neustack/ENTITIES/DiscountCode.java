package com.kgstrivers.neustack.ENTITIES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class DiscountCode {
    private String id;
    private String code;
    private double percentage;
    private boolean isUsed;
    private Order generatedForOrder; // New field for order association
    private Order usedByOrder; // New field for order association
    private long createdAt;
}
