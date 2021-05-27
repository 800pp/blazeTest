package com.blaze.cofran.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Currency;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "order")
public class Order {
    @Id
    private String id;

    @NotNull
    @Min(value = 0,message = "Must be greater than $min")
    private long orderNumber;

    @NotEmpty
    private String status;

    private LocalDate date = LocalDate.now();

    @NotEmpty
    private String customer;

    private Double totalTaxes;

    private Double totalAmount;

    @DBRef
    private List<ProductOrder> items;

    @PersistenceConstructor
    public Order(long orderNumber,String status,String customer,Double totalTaxes,Double totalAmount,List<ProductOrder> items){
        this.orderNumber = orderNumber;
        this.status = status;
        this.customer = customer;
        this.totalTaxes = totalTaxes;
        this.totalAmount = totalAmount;
        this.items = items;
    }

}
