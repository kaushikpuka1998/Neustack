package com.kgstrivers.neustack.SERVICES;

import com.kgstrivers.neustack.ENTITIES.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    private final Map<String, CartItem> items = new HashMap<>();
    private final DiscountService discountService; // New import
    private final ProductService productService;

    @Autowired
    public CartService(DiscountService discountService, ProductService productService) {
        this.discountService = discountService;
        this.productService = productService;
    }

    public void addItem(String productId, int quantity, String userId) {
        String key = productId + "-" + userId;
        Product product = productService.getProductById(productId);

        if (items.containsKey(key)) {
            if( productService.getProductById(productId).getStock() < quantity){
                throw new RuntimeException("Insufficient stock for product: " + productId);
            }
            items.get(key).setQuantity(items.get(key).getQuantity() + quantity);
            productService.reduceStock(productId, quantity);

        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);

            product = productService.getProductById(productId); // Assuming you have this method
            if (product != null) {
                int availableStock = product.getStock(); // Assume getStock is a method in the Product entity

                if (availableStock >= quantity) {
                    // Reduce stock
                    productService.reduceStock(productId, quantity);
                    newItem.setPrice(product.getPrice() * quantity);
                    newItem.setQuantity(quantity);
                    newItem.setUserId(userId);
                    newItem.setId(UUID.randomUUID().toString());
                    items.put(key, newItem);
                } else {
                    throw new RuntimeException("Insufficient stock for product: " + productId);
                }
            } else {
                throw new RuntimeException("Product not found");
            }
        }
    }

    public int getTotalItems() {
        return items.size();
    }

    public List<CartItem> getItems(String userId) { // Updated to accept user ID
        List<CartItem> userCartItems = new ArrayList<>();
        for (Map.Entry<String, CartItem> entry : items.entrySet()) {
            if (entry.getValue().getUserId().equals(userId)) {
                userCartItems.add(entry.getValue());
            }
        }
        return userCartItems;
    }

    public double calculateTotalPrice(String userId) { // Method to calculate total price for the user's cart
        List<CartItem> items = getItems(userId);
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

        for (CartItem item : items.values()) {
            OrderItem orderItem = new OrderItem(item.getProductId(), item.getQuantity(),item.getPrice());
            order.addItem(orderItem);
        }

        return order;
    }

    private String generateOrderId() {
        // Simulate an order ID generator for simplicity
        return UUID.randomUUID().toString();
    }


}