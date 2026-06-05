package com.kgstrivers.neustack.SERVICES;

import com.kgstrivers.neustack.ENTITIES.DiscountCode;
import com.kgstrivers.neustack.ENTITIES.Order;
import com.kgstrivers.neustack.REPOSITORIES.CUSTOMREPOSITORIES.DiscountInMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class DiscountService {

    @Autowired
    DiscountInMemoryRepository discountInMemoryRepository;

    @Autowired
    UserService userService;

    public DiscountCode generateDiscountCode(int everyNthOrder, double percentage) {
        // Generate a new discount code and add it to the map
        DiscountCode discountCode = new DiscountCode(everyNthOrder, percentage);// Example discount code
        return discountInMemoryRepository.save(discountCode);

    }

    public DiscountCode getActiveDiscount(String code, String userId) {
      Optional<DiscountCode> d = discountInMemoryRepository.findAll().stream().filter(discountCode -> Objects.equals(discountCode.getCode(), code)).findFirst();
        int orderCount = userService.getUserOrders(userId).size();

      if(orderCount>0 && orderCount%d.get().getEveryNthOrder() == 0){
          return d.get();
      }else{
          throw new RuntimeException("Discount code not Applicable");
      }
    }
}