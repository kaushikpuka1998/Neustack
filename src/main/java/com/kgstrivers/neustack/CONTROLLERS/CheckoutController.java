package com.kgstrivers.neustack.CONTROLLERS;

import com.kgstrivers.neustack.SERVICES.CartService;
import com.kgstrivers.neustack.SERVICES.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

//    private final DiscountService discountService;

//    @Autowired
//    public CheckoutController( DiscountService discountService) {
//        this.discountService = discountService;
//    }

//    @PostMapping("/validate/{code}")
//    public boolean validateDiscountCode(@PathVariable String code) {
//        return discountService.validateDiscountCode(code);
//    }
}
