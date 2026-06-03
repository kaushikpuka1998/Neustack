package com.kgstrivers.neustack.CONTROLLERS;

import com.kgstrivers.neustack.ENTITIES.ApiResponse;
import com.kgstrivers.neustack.SERVICES.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final CartService cartService;

    @GetMapping("/cart/{userId}")
    public ResponseEntity<ApiResponse<Double>> calculateTotalPrice(@PathVariable String userId) {
        try {
            double totalPrice = cartService.calculateTotalPrice(userId);
            return new ResponseEntity<>(new ApiResponse<>(true, totalPrice), HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Error calculating total price: " + e.getMessage();
            return new ResponseEntity<>(new ApiResponse<>(false, null, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Other admin methods
}


