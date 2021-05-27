package com.blaze.cofran.services;

import com.blaze.cofran.entity.Product;
import com.blaze.cofran.repository.ProductReporsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    @Autowired
    ProductReporsitory productReporsitory;

    public Iterable<Product> findAll(){
        return productReporsitory.findAll();
    }

    public Page<Product> search(String category,Integer pageNo,Integer pageSize,String sortBy, Boolean isAscending){
        Pageable paging = !isAscending ? PageRequest.of(pageNo,pageSize, Sort.by(sortBy).descending()) : PageRequest.of(pageNo,pageSize, Sort.by(sortBy).ascending());
        return (category.equals("null")) ? productReporsitory.findAll(paging) : productReporsitory.findByCategory(category,paging);
    }
    public Product save(Product product){
        return productReporsitory.save(product);
    }
    public Product findById(Product product){
        return productReporsitory.findById(product.getId()).orElse(null);
    }
    public Product edit(Product product){
        return productReporsitory.save(product);
    }
    public void delete(String productId){
        productReporsitory.deleteById(productId);
    }
}
