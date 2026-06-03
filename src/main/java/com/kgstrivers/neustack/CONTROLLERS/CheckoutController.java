package com.kgstrivers.neustack.CONTROLLERS;

import com.kgstrivers.neustack.ENTITIES.AddCartItemRequest;
import com.kgstrivers.neustack.SERVICES.CartService;
import com.kgstrivers.neustack.SERVICES.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    @Autowired
    private final DiscountService discountService;

    @Autowired
    private final CartService cartService;

//    @PostMapping("/validate/{code}")
//    public boolean validateDiscountCode(@PathVariable String code) {
//        return discountService.validateDiscountCode(code);
//    }
}
