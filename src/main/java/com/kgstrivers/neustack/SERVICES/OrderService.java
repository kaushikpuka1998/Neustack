package com.kgstrivers.neustack.SERVICES;

import com.kgstrivers.neustack.ENTITIES.Order;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.OrderInMemoryRepository;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.UserRepositoryInMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    private final OrderInMemoryRepository orderInMemoryRepository;

    @Autowired
    private final CartService cartService;

    @Autowired
    private final UserRepositoryInMemory userRepository;

    public OrderService(OrderInMemoryRepository orderInMemoryRepository, CartService cartService, UserRepositoryInMemory userRepository) {
        this.orderInMemoryRepository = orderInMemoryRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    public Order createOrder(String userId, String discountCode) {
        // Check out the cart to get total amount and order items
        return cartService.checkout(userId, discountCode);
    }

    // List of User's Order
    public Order getOrderDetails(String orderId) {
        return orderInMemoryRepository.findById(orderId).get();
    }

}
