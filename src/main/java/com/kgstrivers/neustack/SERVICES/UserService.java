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
        if(userRepository.findAll().stream().anyMatch(u -> u.getName().equals(user.getName()))) {
            throw new RuntimeException("User with id " + user.getName() + " already exists");
        }
        return userRepository.save(user);
    }

    public User findUserName(User user) {
        return userRepository.findAll().stream().anyMatch(u -> u.getName().equals(user.getName())) ? user : null;
    }

    public User findUserNameWithPass(String username, String password) {
        List<User> users = userRepository.findAll();
        return  users.stream().filter(u1 -> u1.getName().equals(username) && u1.getPassword().equals(password)).findFirst().orElse(null);
    }

    public List<Order> getUserOrders(String userId) {
        return userRepository.findById(userId).get().getOrders();
    }
}
