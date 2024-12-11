package com.example.CustomerAPI.repository;

import com.example.CustomerAPI.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity,String> {


}
