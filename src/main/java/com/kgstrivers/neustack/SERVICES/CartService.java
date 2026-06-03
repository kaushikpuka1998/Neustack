package com.kgstrivers.neustack.SERVICES;

import com.kgstrivers.neustack.ENTITIES.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    private final Map<String, Cart> userToCartList = new HashMap<>();
    private final DiscountService discountService; // New import
    private final ProductService productService; // New import

    @Autowired
    public CartService(DiscountService discountService, ProductService productService, DiscountService discountService1, ProductService productService1) {/* implementation omitted for shortness */
        this.discountService = discountService1;
        this.productService = productService1;
    }

    public Cart addItem(String productId, int quantity, String userId) {
        // Implementation to add item to the cart
        Cart cart = Objects.isNull(userToCartList.get(userId)) ? new Cart(userId) : userToCartList.get(userId);
        Product product = productService.getProductById(productId);

        if (cart != null && cart.getCartItems().stream().anyMatch(item -> item.getProductId().equals(productId))) {
            // Update existing cart item
            Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();
            productService.reduceStock(productId, quantity);
            optionalCartItem.ifPresent(cartItem -> {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItem.calculatePrice(cartItem.getQuantity(), product.getPrice()); // Example: setting a new fixed price
            });
        }  else {
            // Add new cart item to the cart
            
            if(product.getStock()<quantity){
                throw new RuntimeException("Stock is unavailable according to your given quantity");
            }
            productService.reduceStock(productId, quantity);
            CartItem cartItem = new CartItem(product, quantity, userId);
            if (cart != null) {
                cart.addCartItem(cartItem); // Assuming addCartItem updates the cart and returns the updated CartItem
            }
            userToCartList.put(userId, cart);
        }
        if (cart != null) {
            cart.setTotalPrice(calculateTotalPrice(userId));
        }
        return cart;
    }

    public Cart getCartList(String userId) {
        return userToCartList.get(userId);
    }

    public double calculateTotalPrice(String userId) { // Method to calculate total price for the user's cart
        List<CartItem> items = userToCartList.get(userId).getCartItems();
        return items.stream()
                .mapToDouble(CartItem::getPrice)
                .sum();
    }

    public Order checkout(String userId, String discountCode) {
        // Calculate total amount
        double totalAmount = calculateTotalPrice(userId);

        // Apply discount if applicable
        int discount = 0;
//        Optional<DiscountCode> activeDiscount = discountService.getActiveDiscount(discountCode);
        Optional<DiscountCode> activeDiscount = null;
        if (activeDiscount.isPresent()) {
            discount = (int) (totalAmount * activeDiscount.get().getPercentage());
        }

        // Calculate final amount
        double finalAmount = totalAmount - discount;

        Order order = new Order(generateOrderId(),String.valueOf(userId), totalAmount, discount, finalAmount,discountCode, new ArrayList<>());

        for (Cart item : userToCartList.values()) {
            for(CartItem cartItem : item.getCartItems()) {
                if (cartItem.getUserId().equals(userId)) {
                    OrderItem orderItem = new OrderItem(cartItem.getProductId(), cartItem.getQuantity(),cartItem.getPrice());
                    order.addItem(orderItem);
                }
            }

        }

        return order;
    }

    private String generateOrderId() {
        // Simulate an order ID generator for simplicity
        return UUID.randomUUID().toString();
    }


}