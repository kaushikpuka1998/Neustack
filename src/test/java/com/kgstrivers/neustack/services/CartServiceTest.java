package com.kgstrivers.neustack.services;

import com.kgstrivers.neustack.ENTITIES.*;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.CartInMemoryRepository;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.OrderInMemoryRepository;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.ProductInMemoryRepository;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.UserRepositoryInMemory;
import com.kgstrivers.neustack.SERVICES.CartService;
import com.kgstrivers.neustack.SERVICES.DiscountService;
import com.kgstrivers.neustack.SERVICES.ProductService;
import com.kgstrivers.neustack.SERVICES.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartServiceTest {

    @Mock
    CartInMemoryRepository cartInMemoryRepository;

    @Mock
    ProductInMemoryRepository productInMemoryRepository;

    @Autowired
    UserRepositoryInMemory userRepository;

    @Mock
    DiscountService discountService;

    @Mock
    ProductService productService;

    @Mock
    OrderInMemoryRepository orderInMemoryRepository;

    @Mock
    UserService userService;

    @InjectMocks
    CartService cartService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testAddProductToCart() {
        String productId = "1";
        int quantity = 1;
        String userId = "USER123";
        Product product = new Product("Computer", 500, 20);
        when(productInMemoryRepository.findById(anyString())).thenReturn(Optional.of(product));
        Cart res = cartService.addItem(productId, quantity, userId);
        assertFalse(res.getCartItems().isEmpty());
    }

    @Test
    public void testAddProductToCartStockUnavailableForNewItem() {
        String productId = "1";
        int quantity = 21;
        String userId = "USER123";
        Product product = new Product("Computer", 500, 20);
        when(productInMemoryRepository.findById(anyString())).thenReturn(Optional.of(product));
        assertThrows(RuntimeException.class, () -> cartService.addItem(productId, quantity, userId));
    }


    @Test
    public void testAddProductToCartStockUnavailableForExistingItem() {
        String productId = "1";
        int quantity = 20;
        String userId = "USER123";
        Product product = new Product("Computer", 500, 20);
        Product product2 = new Product("Computer", 500, 19);
        product.setId(productId);
        Cart cart = new Cart(userId);
        CartItem cartItem = new CartItem(product, 1, userId, userId);
        cart.addCartItem(cartItem);

        when(productInMemoryRepository.save(product)).thenReturn(product2);
        when(cartInMemoryRepository.findById(userId)).thenReturn(Optional.of(cart));
        when(productInMemoryRepository.save(any())).thenReturn(product);
        when(productInMemoryRepository.findById(anyString())).thenReturn(Optional.of(product));

        assertNotNull(cartService.addItem(productId, quantity, userId));
    }
}
