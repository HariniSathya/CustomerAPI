package com.example.CustomerAPI.service;

import com.example.CustomerAPI.repository.CustomerRepository;
import com.example.CustomerAPI.entity.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private final CustomerRepository customerRepository;

    public CustomerEntity createCustomer(final CustomerEntity request) {

        logger.info("Creating customer with email: {}", request.getEmailAddress());

        CustomerEntity customerEntity = CustomerEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .emailAddress(request.getEmailAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();

        CustomerEntity savedCustomer = customerRepository.saveAndFlush(customerEntity);

        logger.info("Customer created with ID: {}", savedCustomer.getId());

        return savedCustomer;

    }

    public Optional<CustomerEntity> findByCustomerId (String id) {

        logger.info("Fetching customer with ID: {}", id);

        Optional<CustomerEntity> customer= customerRepository.findById(String.valueOf(id));

        if (customer.isPresent()) {
            logger.info("Customer found with ID: {}", id);
        } else {
            logger.warn("Customer not found with ID: {}", id);
        }

        return customer;
    }

    public List<CustomerEntity> findAllCustomers(){

        logger.info("Fetching all customers.");

        List<CustomerEntity> customerEntityList= customerRepository.findAll();

        logger.info("Total customers fetched: {}", customerEntityList.size());

        return customerEntityList;
    }

    public CustomerEntity updateCustomer(CustomerEntity customer){

        logger.info("Updating customer with ID: {}", customer.getId());

        Optional<CustomerEntity> existingCustomer = customerRepository.findById(String.valueOf(customer.getId()));
        existingCustomer.ifPresent(updatedCustomer -> {
            updatedCustomer.setFirstName(customer.getFirstName());
            updatedCustomer.setLastName(customer.getLastName());
            updatedCustomer.setMiddleName(customer.getMiddleName());
            updatedCustomer.setEmailAddress(customer.getEmailAddress());
            updatedCustomer.setPhoneNumber(customer.getPhoneNumber());
            customerRepository.save(customer);
            logger.info("Customer with ID: {} updated successfully", customer.getId());
        });

        return existingCustomer.orElse(null);
    }

    public String deleteCustomer(String id){

        logger.info("Attempting to delete customer with ID: {}", id);

        Optional<CustomerEntity> existingCustomer = customerRepository.findById(String.valueOf(id));

        return existingCustomer
                .map(customer -> {
                    customerRepository.deleteById(String.valueOf(id));
                    logger.info("Customer with ID: {} deleted successfully", id);

                    return "Customer with ID " + id + " deleted successfully";
                }).orElse("Customer with ID " + id + " not found");
    }
}
