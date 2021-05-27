package com.blaze.cofran.rest;

import com.blaze.cofran.DTO.PagingResult;
import com.blaze.cofran.entity.Product;
import com.blaze.cofran.services.ProductService;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<PagingResult> getPageProduct(
            @RequestParam(defaultValue = "null") String category,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "false") Boolean isAscending
    ){
        Page<Product> pageResult = productService.search(category,pageNo,pageSize,sortBy,isAscending);

        int counter = 0;
        for(Object i : productService.findAll()){
            counter++;
        }
        int totalPages = (int) Math.floor(counter * 1.0/pageSize);

        if (pageResult.hasContent()){
            return ResponseEntity.ok(new PagingResult(totalPages,pageResult.getContent()));
        }else{
            return ResponseEntity.ok(new PagingResult(0,new ArrayList<>()));
        }
    }


    @PostMapping()
    public ResponseEntity<?> add(@Valid @RequestBody Product product){
        try {
            Product inserted = productService.save(product);
            return ResponseEntity.ok(inserted);
        }
        catch (Exception ex){
            return ResponseEntity.badRequest().body("Something went wrong!: " + ex.getLocalizedMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> edit(@RequestBody Product product){
        if (product.getId() == null){return ResponseEntity.badRequest().body("No product id specified."); }
        Product thisProduct = productService.findById(product);
        if (thisProduct == null) { return ResponseEntity.ok("No product found.");}
        Product savedProduct = productService.edit(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PostMapping("/find")
    public ResponseEntity<?> find(@RequestBody Product product){
        if (product.getId() == null){return ResponseEntity.badRequest().body("No product id specified."); }
        Product thisProduct = productService.findById(product);
        if (thisProduct == null) { return ResponseEntity.ok("No product found.");}
        return ResponseEntity.ok(thisProduct);
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@RequestBody Product product){
        if (product.getId() == null){return ResponseEntity.badRequest().body("No product id specified."); }
        Product thisProduct = productService.findById(product);
        if (thisProduct == null) { return ResponseEntity.ok("No product found.");}
        productService.delete(product.getId());
        return ResponseEntity.ok("Product successfully deleted");
    }

    //ErrorHandling when POST product is invalid!
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    //ErrorHandler when field input type is wrong!
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public Map<String, String> handleHttpMessageNotReadableExceptions(
            InvalidFormatException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error",ex.getValue().toString() + " is not a " + ex.getTargetType().getSimpleName());
        return errors;
    }
}
