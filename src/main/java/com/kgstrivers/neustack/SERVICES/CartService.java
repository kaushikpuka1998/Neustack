package com.kgstrivers.neustack.SERVICES;

import com.kgstrivers.neustack.ENTITIES.*;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.CartInMemoryRepository;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.OrderInMemoryRepository;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.ProductInMemoryRepository;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.UserRepositoryInMemory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private CartInMemoryRepository cartInMemoryRepository;

    @Autowired
    private ProductInMemoryRepository productInMemoryRepository;

    @Autowired
    private UserRepositoryInMemory userRepository;

    @Autowired
    private final DiscountService discountService;
    @Autowired
    private final ProductService productService;

    @Autowired
    private final OrderInMemoryRepository orderInMemoryRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    public CartService(DiscountService discountService1, ProductService productService1, CartInMemoryRepository cartInMemoryRepository, ProductInMemoryRepository productInMemoryRepository, OrderInMemoryRepository orderInMemoryRepository, UserService userService) {
        this.discountService = discountService1;
        this.productService = productService1;
        this.cartInMemoryRepository = cartInMemoryRepository;
        this.productInMemoryRepository = productInMemoryRepository;
        this.orderInMemoryRepository = orderInMemoryRepository;
        this.userService = userService;
    }

    public Cart addItem(String productId, int quantity, String userId) {
        // Implementation to add item to the cart
        Cart cart = getOrCreateCart(userId);
        Product product = productInMemoryRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        if (cart.getCartItems().stream().anyMatch(item -> item.getProductId().equals(productId))) {
            // Update existing cart item
            Optional<CartItem> optionalCartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProductId().equals(productId))
                    .findFirst();

            if (product.getStock() < quantity) {
                throw new RuntimeException("Stock is unavailable according to your given quantity");
            }
            productService.reduceStock(productId, quantity);
            optionalCartItem.ifPresent(cartItem -> {
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItem.calculatePrice(cartItem.getQuantity(), product.getPrice()); // Example: setting a new fixed price
            });
        } else {
            if (product.getStock() < quantity) {
                throw new RuntimeException("Stock is unavailable according to your given quantity");
            }
            productService.reduceStock(productId, quantity);
            CartItem cartItem = new CartItem(product, quantity, userId, cart.getId());
            cart.addCartItem(cartItem);
        }
        cart.setTotalPrice(calculateTotalPrice(userId));
        cartInMemoryRepository.save(cart);
        return cart;
    }

    public Optional<Cart> getCartList(String userId) {
        return cartInMemoryRepository.findById(userId);
    }

    public Cart removeProductFromCart(String userId, String productId) {
        Optional<Cart> cart = cartInMemoryRepository.findById(userId);
        List<CartItem> cartItems = cart.get().getCartItems();

        if(cartItems.stream().noneMatch(item -> item.getProductId().equals(productId))){
            throw new RuntimeException("Product not found: " + productId);
        }

        productService.reduceStock(productId, -cartItems.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .map(CartItem::getQuantity)
                .orElse(0));

        cartItems.removeIf(item -> item.getProductId().equals(productId));

        cart.get().setCartItems(cartItems);
        cartInMemoryRepository.save(cart.get());
        return cart.get();
    }

    public double calculateTotalPrice(String userId) { // Method to calculate total price for the user's cart
        Optional<Cart> cartOpt = cartInMemoryRepository.findById(userId);
        if (cartOpt.isEmpty()) {
            return 0.0;
        }
        List<CartItem> items = cartOpt.get().getCartItems();
        return items.stream()
                .mapToDouble(CartItem::getPrice)
                .sum();
    }

    public Order checkout(String userId, String discountCode) {
        // Calculate total amount
        double totalAmount = calculateTotalPrice(userId);
        // Apply discount if applicable
        int discount = 0;
        List<Order> orderCount = userRepository.findById(userId).get().getOrders();
        Optional<DiscountCode> activeDiscount = discountService.getActiveDiscount(discountCode);
        if (activeDiscount.isPresent() && !orderCount.isEmpty() && (orderCount.size() % (activeDiscount.get().getEveryNthOrder()) == 0)) {
            discount = activeDiscount.map(code -> (int) (totalAmount * (1- code.getXPercentage()/100))).orElse(0);
        }

        // Calculate final amount
        double finalAmount = totalAmount - discount;
        Cart cart = cartInMemoryRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        Order order = new Order(UUID.randomUUID().toString(), String.valueOf(userId), totalAmount, discount, finalAmount, discountCode, new ArrayList<>());

        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem.getUserId().equals(userId)) {
                OrderItem orderItem = new OrderItem(cartItem.getProductId(), cartItem.getQuantity(), cartItem.getPrice(), cartItem.getName(), cartItem.getImgUrl());
                order.addItem(orderItem);
            }
        }


        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        orderInMemoryRepository.save(order);
        user.getOrders().add(order);
        cart.emptyCart();
        cartInMemoryRepository.save(cart);

        return order;
    }

    public Cart getOrCreateCart(String userId) {
        Optional<Cart> optionalCart = cartInMemoryRepository.findById(userId);

        // If the cart exists, return it
        if (optionalCart.isPresent()) {
            return optionalCart.get();
        }

        // If the cart does not exist, create a new one and persist it
        Cart newCart = new Cart(userId);
        cartInMemoryRepository.save(newCart);
        return newCart;
    }
}