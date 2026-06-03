package com.kgstrivers.neustack.SERVICES;

import com.kgstrivers.neustack.ENTITIES.Product; // Assuming Product entity exists in ENTITIES package
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    private final Map<String, Product> products;

    @Autowired
    public ProductService() {
        this.products = new HashMap<>();
    }

    /**
     * Adds a product to the collection.
     *
     * @param product   the Product object to be added
     */
    public Product addProduct(Product product) {

        String id = UUID.randomUUID().toString();
        product.setId(id);
        products.put(id, product);
        return products.get(product.getId());
    }



    /**
     * Fetches a product by its ID.
     *
     * @param productId the unique identifier of the product
     * @return the Product object if found, otherwise null
     */
    public Product getProductById(String productId) {
        return products.get(productId);
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public void updateProduct(Product product) {
        if(!products.containsKey(product.getId())) {
            throw new IllegalArgumentException("Product does not exist");
        }
        products.put(product.getId(), product);
    }

    public Product reduceStock(String productId, int quantity) {
        if(!products.containsKey(productId)) {
            throw new IllegalArgumentException("Product does not exist");
        }
        Product product = products.get(productId);
        product.setStock(product.getStock()-quantity);
        return products.put(product.getId(), product);
    }
}