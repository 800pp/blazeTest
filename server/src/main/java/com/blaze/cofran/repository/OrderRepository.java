package com.blaze.cofran.repository;

import com.blaze.cofran.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    Page<Order> findByCustomerLike(String customer, Pageable pageable);
}
