package com.kgstrivers.neustack.CONTROLLERS;

import com.kgstrivers.neustack.ENTITIES.ApiResponse;
import com.kgstrivers.neustack.ENTITIES.CheckoutRequest;
import com.kgstrivers.neustack.ENTITIES.Order;
import com.kgstrivers.neustack.SERVICES.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    @Autowired
    private final CartService cartService;

//    @PostMapping("/validate/{code}")
//    public boolean validateDiscountCode(@PathVariable String code) {
//        return discountService.validateDiscountCode(code);
//    }

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody CheckoutRequest request) {
        try {
            Order order = cartService.checkout(request.getUserId(), request.getDiscountCode());
            return new ResponseEntity<>(new ApiResponse<>(true, order), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(false, null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
