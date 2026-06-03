package com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES;

import com.kgstrivers.neustack.ENTITIES.Product;
import com.kgstrivers.neustack.REPOSITORIES.InMemoryRepositoryBase;
import org.springframework.stereotype.Repository;

@Repository
public class ProductInMemoryRepository extends InMemoryRepositoryBase<Product> {
}
