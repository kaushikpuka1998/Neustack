package com.kgstrivers.neustack.ENTITIES;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PurchaseDetails {
    private final int totalItems;
    private final double revenue; // Placeholder for actual revenue calculation
    private final int discountCodesGenerated;


}
