package com.kgstrivers.neustack.ENTITIES;

import com.kgstrivers.neustack.REPOSITORIES.HasId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Cart implements HasId {
    private String id;
    private String userId;
    private List<CartItem> cartItems = new ArrayList<>();
    private double totalPrice;

    public Cart(String userId){
        this.id =  userId;
        this.userId = userId;
    }

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
    }

    public int getTotalQuantity() {
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Override
    public String getId() {
        return id;
    }
}
