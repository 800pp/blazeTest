package com.blaze.cofran.rest;

import com.blaze.cofran.entity.Order;
import com.blaze.cofran.services.OrderService;
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

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @GetMapping
    public ResponseEntity<?> search(@RequestParam(defaultValue = "null") String customer,
                                    @RequestParam(defaultValue = "0") Integer pageNo,
                                    @RequestParam(defaultValue = "5") Integer pageSize,
                                    @RequestParam(defaultValue = "id") String sortBy,
                                    @RequestParam(defaultValue = "false") Boolean isAscending)
    {
        Page<Order> pageResult = orderService.search(customer,pageNo,pageSize,sortBy,isAscending);
        if (pageResult.hasContent()){
            return ResponseEntity.ok(pageResult.getContent());
        }else{
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @PostMapping("/find")
    public ResponseEntity<?> findOne(@RequestBody Order order){
        Order thisOrder = orderService.findById(order);
        if (thisOrder == null) return ResponseEntity.badRequest().body("Order not found");
        return ResponseEntity.ok(thisOrder);
    }

    @PostMapping("/manageOrder")
    public ResponseEntity<?> edit(@RequestBody Order order){
        Order thisOrder = orderService.findById(order);

        if (thisOrder == null) return ResponseEntity.badRequest().body("Order not found");
        thisOrder.setStatus(order.getStatus());
        return ResponseEntity.ok(orderService.edit(thisOrder));
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Order order){
        try{
            if (order.getItems().size() == 0 || order.getItems() == null){
                return ResponseEntity.badRequest().body("No items list found!");
            }
            return ResponseEntity.ok(orderService.addOrder(order));
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
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
