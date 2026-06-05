package com.kgstrivers.neustack.ENTITIES;

import lombok.Data;

@Data
public class RemoveProductFromCartRequest {
    String cartId;
    String productId;
}
