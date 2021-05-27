package com.blaze.cofran.repository;

import com.blaze.cofran.entity.ProductOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOrderRepository extends MongoRepository<ProductOrder,String> {
}
