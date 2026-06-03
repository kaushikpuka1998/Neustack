package com.kgstrivers.neustack.CONTROLLERS;

import com.kgstrivers.neustack.ENTITIES.AddCartItemRequest;
import com.kgstrivers.neustack.ENTITIES.CartItem;
import com.kgstrivers.neustack.ENTITIES.ItemCount;
import com.kgstrivers.neustack.SERVICES.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public String addItem(@RequestBody AddCartItemRequest request) {
        cartService.addItem(request.getProductId(), request.getQuantity(), request.getUserId());
        return "Item added successfully";
    }

    @GetMapping("/total-items")
    public int getTotalItems() {
        return cartService.getTotalItems();
    }

    @GetMapping("/items/{user_id}")
    public List<CartItem> getItems(@PathVariable String user_id) { // Updated to return CartItem
        return cartService.getItems(user_id); // Assuming items are now of type CartItem
    }
}
