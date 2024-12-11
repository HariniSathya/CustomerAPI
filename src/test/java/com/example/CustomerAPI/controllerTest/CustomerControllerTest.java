package com.example.CustomerAPI.controllerTest;

import com.example.CustomerAPI.controller.CustomerController;
import com.example.CustomerAPI.entity.CustomerEntity;
import com.example.CustomerAPI.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest(classes = CustomerController.class)
@AutoConfigureMockMvc
@AutoConfigureWebMvc
public class CustomerControllerTest {

    private static final  String BASE_URL ="/customer-service";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CustomerService customerService;
    private PodamFactory podamFactory;
    private CustomerEntity mockCustomer;
    private List<CustomerEntity> mockCustomerList;

    @BeforeEach
    public void setup() throws Exception {
        podamFactory = new PodamFactoryImpl();
        mockCustomer = podamFactory.manufacturePojo(CustomerEntity.class);
        mockCustomer.setId(1L);
    }

    @Test
    @DisplayName("Create Customer - Success")
    public void testCreateCustomer() throws Exception {
        String mockCustomerJson = new ObjectMapper().writeValueAsString(mockCustomer);

        when(customerService.createCustomer(eq(mockCustomer))).thenReturn(mockCustomer);

        mockMvc.perform(post(BASE_URL + "/save")
                .accept(MediaType.APPLICATION_JSON)
                .content(mockCustomerJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated());

        verify(customerService,times(1)).createCustomer(eq(mockCustomer));
    }

    @Test
    @DisplayName("Create Customer - Failure Test")
    void createCustomerFailure() throws Exception {

        mockMvc.perform(post(BASE_URL + "/save")).andExpect(status().isBadRequest());
        verify(customerService, never()).createCustomer(eq(mockCustomer));
    }

    @Test
    @DisplayName("Get Customer Details by Id - Success")
    public void testGetCustomerById() throws Exception{

        when(customerService.findByCustomerId(String.valueOf(eq(mockCustomer.getId())))).thenReturn(Optional.ofNullable(mockCustomer));

        mockMvc.perform(get(BASE_URL + "/getCustomer")
                        .param("id", String.valueOf(mockCustomer.getId()))
                ).andExpect(status().isOk());

        verify(customerService,times(1)).findByCustomerId(anyString());
    }

    @Test
    @DisplayName("Get Customer By Id- Failure Test")
    void getCustomerByIdFailure() throws Exception {

        String customerId = "nonExistentId";

        when(customerService.findByCustomerId(eq(customerId))).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/getCustomer")
                .param("asdf", String.valueOf(mockCustomer.getId()))
        ).andExpect(status().isBadRequest());
        verify(customerService, never()).findByCustomerId(anyString());
    }

    @Test
    @DisplayName("Get All Customer Details - Success")
    public void testGetAllCustomer() throws Exception{

        mockCustomerList = podamFactory.manufacturePojo(ArrayList.class,CustomerEntity.class);

        when(customerService.findAllCustomers()).thenReturn(mockCustomerList);

        mockMvc.perform(get(BASE_URL + "/getAllCustomers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(customerService,times(1)).findAllCustomers();
    }

    @Test
    @DisplayName("Update Customer Details - Success")
    public void testUpdateCustomer() throws Exception{

        String mockCustomerJson = new ObjectMapper().writeValueAsString(mockCustomer);

        when(customerService.updateCustomer(mockCustomer)).thenReturn(mockCustomer);

        mockMvc.perform(put(BASE_URL + "/updateCustomer")
                .accept((MediaType.APPLICATION_JSON))
                .content(mockCustomerJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(customerService,times(1)).updateCustomer(eq(mockCustomer));
    }

    @Test
    @DisplayName("Update Customer Details - Failure")
    public void testUpdateCustomerFailure() throws Exception{

        String mockCustomerJson = new ObjectMapper().writeValueAsString(mockCustomer);

        when(customerService.updateCustomer(mockCustomer)).thenReturn(null);

        mockMvc.perform(put(BASE_URL + "/updateCustomer")
                .accept((MediaType.APPLICATION_JSON))
                .content(mockCustomerJson)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());

        verify(customerService,times(1)).updateCustomer(eq(mockCustomer));
    }

    @Test
    @DisplayName("Delete Customer Details - Success")
    public void testDeleteCustomer() throws Exception{

        String successMessage = "Customer with ID " + mockCustomer.getId() + " deleted successfully";

        when(customerService.deleteCustomer(String.valueOf(mockCustomer.getId()))).thenReturn(successMessage);

        mockMvc.perform(delete(BASE_URL + "/deleteCustomer")
                .param("id", String.valueOf(mockCustomer.getId()))
        ).andExpect(status().isOk()).andExpect(content().string(successMessage));


        verify(customerService,times(1)).deleteCustomer(anyString());
    }

    @Test
    @DisplayName("Delete Customer Details - Failure")
    public void testDeleteCustomerFailure() throws Exception{

        String failureMessage = "Customer with ID " + mockCustomer.getId() + " not found";

        when(customerService.deleteCustomer(String.valueOf(mockCustomer.getId()))).thenReturn(failureMessage);

        mockMvc.perform(delete(BASE_URL + "/deleteCustomer")
                .param("id", String.valueOf(mockCustomer.getId()))
        ).andExpect(status().isOk()).andExpect(content().string(failureMessage));


        verify(customerService,times(1)).deleteCustomer(anyString());
    }

}
