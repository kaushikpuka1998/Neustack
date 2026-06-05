package com.kgstrivers.neustack.ENTITIES;

import lombok.Data;

@Data
public class DiscountCodeRequest {
    String userId;
    String code;
}
