package com.kgstrivers.neustack.ENTITIES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequest {
    private String userId;
    private String discountCode; // Optional discount code
}

