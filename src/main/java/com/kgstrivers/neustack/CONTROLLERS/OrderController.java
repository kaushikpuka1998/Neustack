package com.kgstrivers.neustack.CONTROLLERS;

import com.kgstrivers.neustack.ENTITIES.ApiResponse;
import com.kgstrivers.neustack.ENTITIES.Order;
import com.kgstrivers.neustack.SERVICES.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable String orderId) {
        try{
            Order addedProduct = orderService.getOrderDetails(orderId);
            return new ResponseEntity<>(new ApiResponse<>(true, addedProduct), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(new ApiResponse<>(false, null, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
