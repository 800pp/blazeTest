package com.blaze.cofran.repository;

import com.blaze.cofran.entity.Product;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReporsitory extends MongoRepository<Product, String> {
    Page<Product> findByCategory(String category, Pageable pageable);
}
