package com.kgstrivers.neustack.CONTROLLERS;

import com.kgstrivers.neustack.ENTITIES.AddCartItemRequest;
import com.kgstrivers.neustack.ENTITIES.ApiResponse;
import com.kgstrivers.neustack.ENTITIES.Cart;
import com.kgstrivers.neustack.ENTITIES.CartItem;
import com.kgstrivers.neustack.SERVICES.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Cart>> addItemToCart(@RequestBody AddCartItemRequest request) {
        try {
            Cart cart = cartService.addItem(request.getProductId(), request.getQuantity(), request.getUserId());
            return new ResponseEntity<>(new ApiResponse<>(true, cart), HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = "Error adding item to cart: " + e.getMessage();
            return new ResponseEntity<>(new ApiResponse<>(false, null, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCartItems(@PathVariable String userId) {
        try {
            Cart cart = cartService.getCartList(userId);
            if (ObjectUtils.isEmpty(cart)) {
                return new ResponseEntity<>(new ApiResponse<>(false, null, "No items in cart"), HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(new ApiResponse<>(true, cart.getCartItems()), HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage = "Error fetching cart items: " + e.getMessage();
            return new ResponseEntity<>(new ApiResponse<>(false, null, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Other cart methods
}