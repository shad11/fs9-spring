package com.bank.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bank.dto.AccountResponse;
import com.bank.dto.CustomerRequest;
import com.bank.dto.CustomerResponse;
import com.bank.enums.Currency;
import com.bank.service.AccountService;
import com.bank.service.CustomerService;
import com.bank.util.RestPageImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {
    @Mock
    private CustomerService customerService;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private CustomerResponse customerResponse;
    private CustomerRequest customerRequest;
    private AccountResponse accountResponse;

    private final String email = "john.doe@example.com";
    private final String name = "John Doe";
    private final int age = 30;
    private final String phone = "1234567890";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        customerResponse = new CustomerResponse(1, name, email, age, phone, new ArrayList<>(), new HashSet<>());

        customerRequest = new CustomerRequest();
        customerRequest.setEmail(email);
        customerRequest.setPassword("password123");
        customerRequest.setName(name);
        customerRequest.setAge(age);
        customerRequest.setPhone(phone);

        accountResponse = new AccountResponse(1, "1234567890", Currency.USD, 0);
    }

    @Test
    void testGetAll() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<CustomerResponse> customers = List.of(customerResponse);
        RestPageImpl<CustomerResponse> pageResponse = new RestPageImpl<>(customers, 0, 10, (long) customers.size());

        when(customerService.getAll(pageable)).thenReturn(pageResponse);

        mockMvc.perform(get("/customers?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email").value(email))
                .andExpect(jsonPath("$.content[0].name").value(name));

        verify(customerService, times(1)).getAll(pageable);
    }

    @Test
    void testGetById() throws Exception {
        long customerId = 1;

        when(customerService.getById(customerId)).thenReturn(customerResponse);

        mockMvc.perform(get("/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.phone").value(phone));

        verify(customerService, times(1)).getById(customerId);
    }

    @Test
    void testCreateSuccess() throws Exception {
        when(customerService.save(customerRequest)).thenReturn(customerResponse);

        mockMvc.perform(post("/customers")
                .contentType("application/json")
                .content("{\"email\":\"" + email + "\", \"password\":\"password123\", \"name\":\"" + name + "\", \"age\":" + age + ", \"phone\":\"" + phone + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.phone").value(phone))
                .andExpect(jsonPath("$.accounts").isEmpty())
                .andExpect(jsonPath("$.employers").isEmpty());

        verify(customerService, times(1)).save(any(CustomerRequest.class));
    }

    @Test
    void testCreateBadRequest() throws Exception {
        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"\", \"password\":\"\", \"name\":\"\", \"age\":0, \"phone\":\"\"}"))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).save(any(CustomerRequest.class));
    }

    @Test
    void testUpdateSuccess() throws Exception {
        long customerId = 1L;
        CustomerRequest updateRequest = new CustomerRequest(1, "newEmail@gmail.com", "newPassword", "New Name", 35, "16124040290");

        customerResponse.setEmail(updateRequest.getEmail());
        customerResponse.setName(updateRequest.getName());
        customerResponse.setAge(updateRequest.getAge());
        customerResponse.setPhone(updateRequest.getPhone());

        when(customerService.update(eq(customerId), any(CustomerRequest.class))).thenReturn(customerResponse);

        mockMvc.perform(put("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(updateRequest.getEmail()))
                .andExpect(jsonPath("$.name").value(updateRequest.getName()));

        verify(customerService, times(1)).update(eq(customerId), any(CustomerRequest.class));
    }

    @Test
    void testUpdateInvalidPhone() throws Exception {
        long customerId = 1L;

        customerRequest.setPhone("0987654321");

        mockMvc.perform(put("/customers/{id}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(customerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDelete() throws Exception {
        long customerId = 1L;

        mockMvc.perform(delete("/customers/{id}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.message").value("Customer deleted"));

        verify(customerService, times(1)).delete(customerId);
    }

    @Test
    void testAddAccountSuccess() throws Exception {
        long customerId = 1L;

        when(accountService.addAccount(eq(customerId), any())).thenReturn(accountResponse);

        mockMvc.perform(post("/customers/{id}/accounts", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currency\":\"USD\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(accountResponse.getNumber()))
                .andExpect(jsonPath("$.currency").value(accountResponse.getCurrency().toString()));

        verify(accountService, times(1)).addAccount(eq(customerId), any());
    }

    @Test
    void testAddAccountMissingCurrency() throws Exception {
        mockMvc.perform(post("/customers/{id}/accounts", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(true))
                .andExpect(jsonPath("$.message").value("Currency is mandatory"));

        verify(accountService, never()).addAccount(anyLong(), any());
    }

    @Test
    void testDeleteAccount() throws Exception {
        long customerId = 1L;
        long accountId = 10L;

        mockMvc.perform(delete("/customers/{id}/accounts/{accountId}", customerId, accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false))
                .andExpect(jsonPath("$.message").value("Account deleted"));

        verify(accountService, times(1)).delete(accountId);
    }
}
