package com.kgstrivers.neustack.SERVICES;

import com.kgstrivers.neustack.ENTITIES.DiscountCode;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.DiscountInMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class DiscountService {

    @Autowired
    DiscountInMemoryRepository discountInMemoryRepository;

    public DiscountCode generateDiscountCode(int everyNthOrder, double percentage) {
        // Generate a new discount code and add it to the map
        DiscountCode discountCode = new DiscountCode(everyNthOrder, percentage);// Example discount code
        return discountInMemoryRepository.save(discountCode);

    }

    public Optional<DiscountCode> getActiveDiscount(String code) {
        return Optional.of(discountInMemoryRepository.findAll().stream().filter(discountCode -> Objects.equals(discountCode.getCode(), code)).findFirst()
                .orElseThrow(() -> new RuntimeException("Discount Code:" + code + " is not available:")));
    }
}