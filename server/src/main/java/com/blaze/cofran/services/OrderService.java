package com.blaze.cofran.services;

import com.blaze.cofran.entity.Order;
import com.blaze.cofran.entity.Product;
import com.blaze.cofran.entity.ProductOrder;
import com.blaze.cofran.repository.OrderRepository;
import com.blaze.cofran.repository.ProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductOrderRepository productOrderRepository;
    @Autowired
    ProductService productService;

    public Order addOrder(Order order) throws Exception {
        double acumTotalAmount = 0.0;
        double acumTotalTaxes = 0.0;
        for (int i = 0; i < order.getItems().size(); i++) {
            ProductOrder productOrder = order.getItems().get(i);
            Product product = productService.findById(productOrder.getProduct());
            if (product == null){
                throw new Exception("N°"+i+" Order product id does not exist");
            }
            productOrder.setProduct(productService.findById(product));
            double itemCost = (productOrder.getQuantity()*productOrder.getProduct().getUnitPrice());
            acumTotalAmount += itemCost;
            productOrder.setCost(itemCost);
            order.getItems().set(i,productOrderRepository.save(productOrder));
        }
        double cityTax = acumTotalAmount*10/100;
        acumTotalTaxes += cityTax;
        double countyTax = (acumTotalAmount + cityTax)*5/100;
        acumTotalTaxes += countyTax;
        double stateTax = (acumTotalAmount + cityTax + countyTax)*8/100;
        acumTotalTaxes += stateTax;
        double federalTaxes = (acumTotalAmount + cityTax + countyTax + stateTax)*2/100;
        acumTotalTaxes += federalTaxes;
        acumTotalAmount += cityTax + countyTax + stateTax + federalTaxes;
        int orderNumber = orderRepository.findAll().size() + 1;
        order.setOrderNumber(orderNumber);
        order.setTotalTaxes(acumTotalTaxes);
        order.setTotalAmount(acumTotalAmount);
        return orderRepository.save(order);
    }

    public Page<Order> search(String customer,Integer pageNo,Integer pageSize,String sortBy, Boolean isAscending){
        Pageable paging = !isAscending ? PageRequest.of(pageNo,pageSize, Sort.by(sortBy).descending()) : PageRequest.of(pageNo,pageSize, Sort.by(sortBy).ascending());
        return (customer.equals("null")) ? orderRepository.findAll(paging) : orderRepository.findByCustomerLike(customer,paging);
    }

    public Order edit(Order order){
        return orderRepository.save(order);
    }

    public Order findById(Order order){
        return orderRepository.findById(order.getId()).orElse(null);
    }

    public void delete(String orderId){
        orderRepository.deleteById(orderId);
    }
}
