package com.bank.controller;

import com.bank.dto.CustomerRequest;
import com.bank.dto.CustomerResponse;
import com.bank.dto.EmployerRequest;
import com.bank.dto.EmployerResponse;
import com.bank.service.CustomerService;
import com.bank.service.EmployerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EmployerControllerTest {
    @Mock
    private EmployerService employerService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private EmployerController employerController;

    private MockMvc mockMvc;
    private EmployerRequest employerRequest;
    private EmployerResponse employerResponse;
    private CustomerResponse customerResponse;
    private CustomerRequest customerRequest;

    private final String name = "Tech Corp";
    private final String address = "123 Tech Lane";
    private final long customerId = 1L;
    private final String customerEmail = "john.doe@example.com";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employerController).build();

        employerRequest = new EmployerRequest();// Set ID for the request to match the response
        employerRequest.setAddress(address);
        employerRequest.setName(name);

        employerResponse = new EmployerResponse(1L, name, address);
        customerRequest = new CustomerRequest();
        customerResponse = new CustomerResponse(customerId, "John Doe", customerEmail, 30, "1234567890", new ArrayList<>(), new HashSet<>());
    }

    @Test
    void testGetAllEmployers() throws Exception {
        when(employerService.getAll()).thenReturn(List.of(employerResponse));

        // Perform the GET request and verify the response
        mockMvc.perform(get("/employers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].address").value(address));
    }

    @Test
    void testCreateSuccess() throws Exception {
        when(employerService.save(employerRequest)).thenReturn(employerResponse);

        mockMvc.perform(post("/employers")
                        .contentType("application/json")
                        .content("{\"name\":\"" + name + "\", \"address\":\"" + address + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.address").value(address));
    }

    @Test
    void testCreateBadRequest() throws Exception {
        mockMvc.perform(post("/employers")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testAddCustomerByIdSuccess() throws Exception {
        customerRequest.setId(customerId);
        when(customerService.getById(customerId)).thenReturn(customerResponse);
        when(employerService.getById(1L)).thenReturn(employerResponse);

        doNothing().when(customerService).addEmployerToCustomer(customerId, employerResponse.getId());

        mockMvc.perform(post("/employers/{id}/add-customer", employerResponse.getId())
                        .contentType("application/json")
                        .content("{\"id\":" + customerId + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer added to employer"));
    }

    @Test
    void testAddCustomerByEmailSuccess() throws Exception {
        customerRequest.setEmail(customerEmail);
        when(customerService.getByEmail(customerEmail)).thenReturn(customerResponse);
        when(employerService.getById(1L)).thenReturn(employerResponse);

        doNothing().when(customerService).addEmployerToCustomer(customerId, employerResponse.getId());

        mockMvc.perform(post("/employers/{id}/add-customer", employerResponse.getId())
                        .contentType("application/json")
                        .content("{\"email\":\"" + customerEmail + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Customer added to employer"));
    }

    @Test
    void testAddCustomerBadRequest() throws Exception {
        mockMvc.perform(post("/employers/{id}/add-customer", employerResponse.getId())
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer email or ID is mandatory"));
    }

    @Test
    void testAddCustomerNotFound() throws Exception {
        customerRequest.setEmail(customerEmail);
        when(customerService.getByEmail(customerEmail)).thenReturn(null);
        when(employerService.getById(1L)).thenReturn(employerResponse);

        mockMvc.perform(post("/employers/{id}/add-customer", employerResponse.getId())
                        .contentType("application/json")
                        .content("{\"email\":\"" + customerEmail + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer not found"));
    }
}
