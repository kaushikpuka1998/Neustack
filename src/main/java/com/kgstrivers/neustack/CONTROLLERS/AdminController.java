package com.kgstrivers.neustack.CONTROLLERS;

import com.kgstrivers.neustack.ENTITIES.ApiResponse;
import com.kgstrivers.neustack.ENTITIES.DiscountCode;
import com.kgstrivers.neustack.ENTITIES.DiscountCodeRequest;
import com.kgstrivers.neustack.ENTITIES.GenerateDiscountCodeRequest;
import com.kgstrivers.neustack.SERVICES.CartService;
import com.kgstrivers.neustack.SERVICES.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final CartService cartService;
    private final DiscountService discountService;

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


    @PostMapping("/generate-discount-code")
    public ResponseEntity<ApiResponse<DiscountCode>> generateDiscountCode(@RequestBody GenerateDiscountCodeRequest request) {
        try {
            DiscountCode newDiscountCode = discountService.generateDiscountCode(request.getEveryNthOrder(), request.getPercentage());
            return new ResponseEntity<>(new ApiResponse<>(true, newDiscountCode), HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = "Error generating discount code: " + e.getMessage();
            return new ResponseEntity<>(new ApiResponse<>(false, null, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/discount-code/")
    public ResponseEntity<ApiResponse<DiscountCode>> getDiscountCode(@RequestBody DiscountCodeRequest request) {
        try {
            DiscountCode discountCode = discountService.getActiveDiscount( request.getCode(), request.getUserId());
            return new ResponseEntity<>(new ApiResponse<>(true, discountCode), HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            return new ResponseEntity<>(new ApiResponse<>(false, null, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Other admin methods
}


