package com.example.CustomerAPI.controller;

import com.example.CustomerAPI.entity.CustomerEntity;
import com.example.CustomerAPI.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path="/customer-service")
public class CustomerController {

    @Autowired
    private final CustomerService customerService;
    @PostMapping(path="/save")
    public ResponseEntity<CustomerEntity> createCustomer(@RequestBody CustomerEntity customer){
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(customer));
    }

    @GetMapping(path="/getCustomer")
    public ResponseEntity<Optional<CustomerEntity>> getCustomerById(@RequestParam final String id){
        return ResponseEntity.ok(customerService.findByCustomerId(id));
    }

    @GetMapping(path="/getAllCustomers")
    public  ResponseEntity<List<CustomerEntity>> getAllCustomers(){
        return ResponseEntity.ok(customerService.findAllCustomers());
    }

    @PutMapping(path="/updateCustomer")
    public ResponseEntity<CustomerEntity> updateCustomer(@RequestBody CustomerEntity customer){
        CustomerEntity updatedCustomer = customerService.updateCustomer(customer);
        if (updatedCustomer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.ok(updatedCustomer);    }

    @DeleteMapping(path="deleteCustomer")
    public ResponseEntity<String> deleteCustomer(@RequestParam final String id){
        return ResponseEntity.ok(customerService.deleteCustomer(id));
    }
}
