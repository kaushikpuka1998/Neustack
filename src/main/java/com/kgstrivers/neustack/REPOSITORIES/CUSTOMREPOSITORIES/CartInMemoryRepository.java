package com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES;

import com.kgstrivers.neustack.ENTITIES.Cart;
import com.kgstrivers.neustack.REPOSITORIES.InMemoryRepositoryBase;
import org.springframework.stereotype.Repository;

@Repository
public class CartInMemoryRepository extends InMemoryRepositoryBase<Cart> {
}
