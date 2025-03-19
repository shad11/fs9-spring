package com.bank.service;

import com.bank.dto.CustomerFacade;
import com.bank.dto.CustomerRequest;
import com.bank.dto.CustomerResponse;
import com.bank.entity.Customer;
import com.bank.entity.Employer;
import com.bank.exception.NotFoundException;
import com.bank.repository.CustomerRepository;
import com.bank.repository.EmployerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EmployerRepository employerRepository;

    @Mock
    private CustomerFacade customerFacade;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    private final String email = "john.doe@example.com";
    private final String name = "John Doe";

    private Customer customer;
    private CustomerRequest customerRequest;
    private CustomerResponse customerResponse;

    @BeforeEach
    void setUp() {
        // Initialize mock data before each test
        customer = new Customer(email, "password123", name, 30, "1234567890", new ArrayList<>(), new HashSet<>());
        customerRequest = new CustomerRequest(1, email, "password123", "John Doe", 30, "1234567890");
        customerResponse = new CustomerResponse(1, name, email, 30, "1234567890", null, null);
    }

    @Test
    void testGetAll() {
        Pageable pageable = Pageable.unpaged();
        List<Customer> customers = List.of(customer);
        Page<Customer> customerPage = new PageImpl<>(customers);

//        given(customerRepository.findAll(pageable)).willReturn(customerPage);
        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        when(customerFacade.toResponse(customer)).thenReturn(customerResponse);

        Page<CustomerResponse> result = customerService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());  // Verify there's exactly 1 customer in the response
        assertEquals(email, result.getContent().getFirst().getEmail());
        assertEquals(name, result.getContent().getFirst().getName());

        verify(customerRepository, times(1)).findAll(pageable);  // Verify findAll is called once
        verify(customerFacade, times(1)).toResponse(customer);  // Verify toResponse is called once for each customer
    }

    @Test
    void testGetById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerFacade.toResponse(customer)).thenReturn(customerResponse);

        CustomerResponse foundCustomer = customerService.getById(1L);

        assertNotNull(foundCustomer);
    }

    @Test
    void testGetByIdNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.getById(1L));
    }

    @Test
    void testGetByEmail() {
        when(customerRepository.findByEmail(email)).thenReturn(customer);
        when(customerFacade.toResponse(customer)).thenReturn(customerResponse);

        CustomerResponse foundCustomer = customerService.getByEmail(email);

        assertNotNull(foundCustomer);
        assertEquals(email, foundCustomer.getEmail());
    }

    @Test
    void testGetByEmailNotFound() {
        when(customerRepository.findByEmail(email)).thenReturn(null);

        CustomerResponse foundCustomer = customerService.getByEmail(email);

        assertNull(foundCustomer);
    }

    @Test
    void testSave() {
        when(customerFacade.toEntity(customerRequest)).thenReturn(customer);
        when(passwordEncoder.encode(customer.getPassword())).thenReturn("encodedPassword");
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerFacade.toResponse(customer)).thenReturn(customerResponse);

        CustomerResponse savedCustomer = customerService.save(customerRequest);

        assertNotNull(savedCustomer);
        assertEquals(email, savedCustomer.getEmail());
        assertEquals(name, savedCustomer.getName());

        verify(customerRepository, times(1)).save(customer); // Verify that save was called once
    }

    @Test
    void testUpdate() {
        CustomerRequest updateRequest = new CustomerRequest(1, "newEmail@gmail.com", "newPassword", "New Name", 35, "380987654321");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(passwordEncoder.encode(updateRequest.getPassword())).thenReturn("encodedNewPassword");

        customer.setEmail(updateRequest.getEmail());
        customer.setPassword("encodedNewPassword");
        customer.setName(updateRequest.getName());
        customer.setAge(updateRequest.getAge());
        customer.setPhone(updateRequest.getPhone());
        customerResponse.setEmail(updateRequest.getEmail());
        customerResponse.setName(updateRequest.getName());
        customerResponse.setAge(updateRequest.getAge());
        customerResponse.setPhone(updateRequest.getPhone());

        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerFacade.toResponse(customer)).thenReturn(customerResponse);

        CustomerResponse updatedCustomer = customerService.update(1L, updateRequest);

        assertNotNull(updatedCustomer);
        assertEquals(updateRequest.getEmail(), updatedCustomer.getEmail());
        assertEquals(updateRequest.getName(), updatedCustomer.getName());
        assertEquals(updateRequest.getAge(), updatedCustomer.getAge());
        assertEquals(updateRequest.getPhone(), updatedCustomer.getPhone());
    }

    @Test
    void testUpdateNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.update(1L, customerRequest));
    }

    @Test
    void testAddEmployerToCustomer() {
        Employer employer = new Employer("Tech Corp", "Software Engineer");

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(employerRepository.findById(1L)).thenReturn(Optional.of(employer));

        customerService.addEmployerToCustomer(1L, 1L);

        assertTrue(customer.getEmployers().contains(employer));

        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testAddEmployerToCustomerNotFoundCustomer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.addEmployerToCustomer(1L, 1L));
    }

    @Test
    void testAddEmployerToCustomerNotFoundEmployer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(employerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.addEmployerToCustomer(1L, 1L));
    }

    @Test
    void testDelete() {
        long customerId = 1L;

        willDoNothing().given(customerRepository).deleteById(customerId);

        customerService.delete(customerId);

        verify(customerRepository, times(1)).deleteById(1L);
    }
}
