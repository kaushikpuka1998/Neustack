package com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES;

import com.kgstrivers.neustack.ENTITIES.User;
import com.kgstrivers.neustack.REPOSITORIES.InMemoryRepositoryBase;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryInMemory extends InMemoryRepositoryBase<User> {
}
