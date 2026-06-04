package com.kgstrivers.neustack.CONTROLLERS;

import com.kgstrivers.neustack.ENTITIES.ApiResponse;
import com.kgstrivers.neustack.ENTITIES.Order;
import com.kgstrivers.neustack.ENTITIES.User;
import com.kgstrivers.neustack.SERVICES.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<User>> addUser(@RequestBody User user) {
        try {
            User createdUser = userService.addUser(user);
            return new ResponseEntity<>(new ApiResponse<>(true, createdUser), HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = "Error adding user: " + e.getMessage();
            return new ResponseEntity<>(new ApiResponse<>(false, null, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable String userId) {
        try {
            User user = userService.getUser(userId);
            if (user != null) {
                return new ResponseEntity<>(new ApiResponse<>(true, user), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponse<>(false, null, "User not found"), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            String errorMessage = "Error retrieving user: " + e.getMessage();
            return new ResponseEntity<>(new ApiResponse<>(false, null, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return new ResponseEntity<>(new ApiResponse<>(true, users), HttpStatus.OK);
        }
        catch (Exception e) {
            String errorMessage = "Error retrieving users: " + e.getMessage();
            return new ResponseEntity<>(new ApiResponse<>(false, null, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userID}/orders")
    public ResponseEntity<ApiResponse<List<Order>>> getAllUsers(@PathVariable String userID) {
        try {
            List<Order> users = userService.getUserOrders(userID);
            return new ResponseEntity<>(new ApiResponse<>(true, users), HttpStatus.OK);
        }
        catch (Exception e) {
            String errorMessage = "Error retrieving users: " + e.getMessage();
            return new ResponseEntity<>(new ApiResponse<>(false, null, errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

