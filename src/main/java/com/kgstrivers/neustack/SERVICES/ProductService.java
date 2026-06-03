package com.kgstrivers.neustack.SERVICES;

import com.kgstrivers.neustack.ENTITIES.Product; // Assuming Product entity exists in ENTITIES package
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.ProductInMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
public class ProductService {

   @Autowired
   private ProductInMemoryRepository productInMemoryRepository;

    /**
     * Adds a product to the collection.
     *
     * @param product   the Product object to be added
     */
    public Product addProduct(Product product) {
        return productInMemoryRepository.save(product);
    }



    /**
     * Fetches a product by its ID.
     *
     * @param productId the unique identifier of the product
     * @return the Product object if found, otherwise null
     */
    public Optional<Product> getProductById(String productId) {
        return productInMemoryRepository.findById(productId);
    }

    public void updateProduct(Product product) {
        if(ObjectUtils.isEmpty(productInMemoryRepository.findById(product.getId()))) {
            throw new IllegalArgumentException("Product does not exist");
        }
        productInMemoryRepository.save(product);
    }

    public Product reduceStock(String productId, int quantity) {
        if(ObjectUtils.isEmpty(productInMemoryRepository.findById(productId))) {
            throw new IllegalArgumentException("Product does not exist");
        }
        Product product = productInMemoryRepository.findById(productId).get();
        product.setStock(product.getStock()-quantity);
        return productInMemoryRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productInMemoryRepository.findAll();
    }
}