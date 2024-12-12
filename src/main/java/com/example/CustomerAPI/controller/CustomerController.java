package com.example.CustomerAPI.controller;

import com.example.CustomerAPI.entity.CustomerEntity;
import com.example.CustomerAPI.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private final CustomerService customerService;
    @PostMapping(path="/save")
    public ResponseEntity<CustomerEntity> createCustomer(@RequestBody CustomerEntity customer){
        CustomerEntity savedCustomer = customerService.createCustomer(customer);
        logger.info("Customer created with ID: {}", savedCustomer.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    @GetMapping(path="/getCustomer")
    public ResponseEntity<Optional<CustomerEntity>> getCustomerById(@RequestParam final String id){

        Optional<CustomerEntity> customer= customerService.findByCustomerId(id);

        if (customer.isPresent()) {
            logger.info("Customer found with ID: {}", id);
        } else {
            logger.warn("Customer not found with ID: {}", id);
        }

        return ResponseEntity.ok(customerService.findByCustomerId(id));
    }

    @GetMapping(path="/getAllCustomers")
    public  ResponseEntity<List<CustomerEntity>> getAllCustomers(){
        for (CustomerEntity customer : customerService.findAllCustomers()) {
            logger.info("Customer List "+ customer.toString());
        }
        return ResponseEntity.ok(customerService.findAllCustomers());
    }

    @PutMapping(path="/updateCustomer")
    public ResponseEntity<CustomerEntity> updateCustomer(@RequestBody CustomerEntity customer){
        CustomerEntity updatedCustomer = customerService.updateCustomer(customer);
        if (updatedCustomer == null) {
            logger.info("Customer with ID "+ customer.getId()+" is not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        logger.info("Updated Customer Details "+ updatedCustomer.toString());

        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping(path="deleteCustomer")
    public ResponseEntity<String> deleteCustomer(@RequestParam final String id){
        String deletedMessage = customerService.deleteCustomer(id);
        logger.info(deletedMessage);
        return ResponseEntity.ok(deletedMessage);
    }
}
