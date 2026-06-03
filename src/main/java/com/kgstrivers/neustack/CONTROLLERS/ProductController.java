package com.kgstrivers.neustack.CONTROLLERS;

import com.kgstrivers.neustack.ENTITIES.Product;
import com.kgstrivers.neustack.SERVICES.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productService.addProduct(product), org.springframework.http.HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> addProduct(@PathVariable String id) {
        return new ResponseEntity<>(productService.getProductById(id), org.springframework.http.HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Product>> allProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), org.springframework.http.HttpStatus.OK);
    }
}





