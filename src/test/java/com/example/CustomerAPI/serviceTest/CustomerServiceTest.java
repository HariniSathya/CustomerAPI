package com.example.CustomerAPI.serviceTest;

import com.example.CustomerAPI.repository.CustomerRepository;
import com.example.CustomerAPI.service.CustomerService;
import com.example.CustomerAPI.entity.CustomerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;
    private CustomerEntity customerEntity;
    private List<CustomerEntity> customerEntityList = new ArrayList<>();
    private static final PodamFactory podamFactory = new PodamFactoryImpl();
    @Captor
    private ArgumentCaptor<CustomerEntity> customerEntityCaptor;

    @BeforeEach
    void beforeEach() {
        customerEntity = podamFactory.manufacturePojo(CustomerEntity.class);

        customerEntityList = podamFactory.manufacturePojoWithFullData(ArrayList.class, CustomerEntity.class);

        customerEntityCaptor = ArgumentCaptor.forClass(CustomerEntity.class);

        customerEntity.setId(1L);

    }

    @Test
    @DisplayName("create customer")
    public void testCreateCustomer(){

        when(customerRepository.saveAndFlush(any(CustomerEntity.class))).thenReturn(customerEntity);

        CustomerEntity result = customerService.createCustomer(customerEntity);

        assertNotNull(result);
        assertEquals(customerEntity.getFirstName(),result.getFirstName());
        assertEquals(customerEntity.getLastName(),result.getLastName());
        assertEquals(customerEntity.getMiddleName(),result.getMiddleName());
        assertEquals(customerEntity.getEmailAddress(),result.getEmailAddress());
        assertEquals(customerEntity.getPhoneNumber(),result.getPhoneNumber());

        verify(customerRepository, times(1)).saveAndFlush(any(CustomerEntity.class));

    }

    @Test
    @DisplayName("Get Customer By Id")
    public void testGetCustomerById() {

        when(customerRepository.findById(String.valueOf(1L))).thenReturn(Optional.of(customerEntity));

        Optional<CustomerEntity> result = customerService.findByCustomerId(String.valueOf(1L));

        assertTrue(result.isPresent(), "Customer should be present");
        assertEquals(customerEntity.getFirstName(), result.get().getFirstName());
        assertEquals(customerEntity.getLastName(), result.get().getLastName());
        assertEquals(customerEntity.getMiddleName(), result.get().getMiddleName());
        assertEquals(customerEntity.getEmailAddress(), result.get().getEmailAddress());
        assertEquals(customerEntity.getPhoneNumber(), result.get().getPhoneNumber());

        verify(customerRepository,times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Get All Customers")
    public void testGetAllCustomers(){
        when(customerRepository.findAll()).thenReturn(customerEntityList);

        List<CustomerEntity> result = customerService.findAllCustomers();

        assertNotNull(result);
        assertEquals(result.size(),customerEntityList.size());

        CustomerEntity customer = customerEntityList.get(0);

        assertEquals(customer.getFirstName(), result.get(0).getFirstName());
        assertEquals(customer.getLastName(), result.get(0).getLastName());
        assertEquals(customer.getMiddleName(), result.get(0).getMiddleName());
        assertEquals(customer.getEmailAddress(), result.get(0).getEmailAddress());
        assertEquals(customer.getPhoneNumber(), result.get(0).getPhoneNumber());

        verify(customerRepository,times(1)).findAll();
    }

    @Test
    @DisplayName("Update customer - Test")
    public void testUpdateCustomerSuccess() {

        CustomerEntity updatedCustomer = podamFactory.manufacturePojo(CustomerEntity.class);
        updatedCustomer.setId(1L);  // Same ID as the existing customer

        when(customerRepository.findById(String.valueOf(1L))).thenReturn(Optional.of(customerEntity));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(updatedCustomer);

        CustomerEntity result = customerService.updateCustomer(updatedCustomer);

        assertNotNull(result);
        assertEquals(updatedCustomer.getFirstName(), result.getFirstName());
        assertEquals(updatedCustomer.getLastName(), result.getLastName());
        assertEquals(updatedCustomer.getEmailAddress(), result.getEmailAddress());
        assertEquals(updatedCustomer.getPhoneNumber(), result.getPhoneNumber());

        verify(customerRepository, times(1)).findById(anyString());
        verify(customerRepository, times(1)).save(updatedCustomer);
    }

    @Test
    @DisplayName("Update Customer - Customer Not Found")
    public void testUpdateCustomerFailure() {

        CustomerEntity updatedCustomer = podamFactory.manufacturePojo(CustomerEntity.class);
        updatedCustomer.setId(0L);

        when(customerRepository.findById(String.valueOf(0))).thenReturn(Optional.empty());

        CustomerEntity result = customerService.updateCustomer(updatedCustomer);

        assertNull(result);

        verify(customerRepository, times(1)).findById(anyString());
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Delete Customer - Success")
    public void testDeleteCustomerSuccess() {

        when(customerRepository.findById(String.valueOf(1L))).thenReturn(Optional.of(customerEntity));

        String result = customerService.deleteCustomer(String.valueOf(1L));

        assertEquals("Customer with ID 1 deleted successfully", result);

        verify(customerRepository, times(1)).findById(anyString());
        verify(customerRepository, times(1)).deleteById(anyString());
    }

    @Test
    @DisplayName("Delete Customer - Customer Not Found")
    public void testDeleteCustomerFailure() {

        when(customerRepository.findById(String.valueOf(0))).thenReturn(Optional.empty());

        String result = customerService.deleteCustomer(String.valueOf(0));

        assertEquals("Customer with ID 0 not found", result);

        verify(customerRepository, times(1)).findById(anyString());
        verify(customerRepository, never()).deleteById(anyString());
    }

}
