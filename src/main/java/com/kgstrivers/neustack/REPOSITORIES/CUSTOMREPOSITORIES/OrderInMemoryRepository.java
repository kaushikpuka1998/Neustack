package com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES;

import com.kgstrivers.neustack.ENTITIES.Order;
import com.kgstrivers.neustack.REPOSITORIES.InMemoryRepositoryBase;
import org.springframework.stereotype.Repository;

@Repository
public class OrderInMemoryRepository extends InMemoryRepositoryBase<Order> {
}
