package com.kgstrivers.neustack.CONTROLLERS;

import com.kgstrivers.neustack.ENTITIES.Order;
import com.kgstrivers.neustack.ENTITIES.PurchaseDetails;
import com.kgstrivers.neustack.SERVICES.CartService;
import com.kgstrivers.neustack.SERVICES.DiscountService;
import com.kgstrivers.neustack.SERVICES.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private final CartService cartService;

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final DiscountService discountService;

    @PostMapping("/generate-discount-code")
    public void generateDiscountCode() {
        discountService.generateDiscountCode();
    }

//    @GetMapping("/purchase-details")
//    public PurchaseDetails getPurchaseDetails() {
//        return new PurchaseDetails(cartService.getTotalItems(), 0, 0);
//    }

    // New method to handle order creation
    @PostMapping("/create-order")
    public Order createOrder(@RequestParam String userId, @RequestParam String discountCode) {
        return orderService.createOrder(userId, discountCode);
    }
}
