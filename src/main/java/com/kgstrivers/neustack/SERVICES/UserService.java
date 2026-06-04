package com.kgstrivers.neustack.SERVICES;

import com.kgstrivers.neustack.ENTITIES.Order;
import com.kgstrivers.neustack.ENTITIES.User;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.UserRepositoryInMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepositoryInMemory userRepository;
    @Autowired
    public UserService(UserRepositoryInMemory userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<Order> getUserOrders(String userId) {
        return userRepository.findById(userId).get().getOrders();
    }
}
