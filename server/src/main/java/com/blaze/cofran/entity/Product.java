package com.blaze.cofran.entity;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Document(collection = "product")
public class Product {
    @Id
    private String id;
    @NotBlank(message = "Must have a name!")
    @Indexed(unique = true)
    private String name;
    @NotBlank(message = "Must have a category!")
    private String category;

    @NotNull
    @Min(value = 0,message = "Must be greater than $min")
    private Float unitPrice;

    @NotNull(message = "Must have a status!")
    private Boolean status;

    @PersistenceConstructor
    public Product(String name,String category,Float unitPrice,Boolean status){
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.status = status;
    }
}
