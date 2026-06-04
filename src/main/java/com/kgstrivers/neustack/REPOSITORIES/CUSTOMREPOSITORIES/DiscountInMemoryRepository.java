package com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES;

import com.kgstrivers.neustack.ENTITIES.DiscountCode;
import com.kgstrivers.neustack.REPOSITORIES.InMemoryRepositoryBase;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
public class DiscountInMemoryRepository extends InMemoryRepositoryBase<DiscountCode> {
}
