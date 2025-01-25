package com.bank.dto;

import com.bank.entity.Customer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerFacade {
    private final ModelMapper modelMapper;

    public CustomerFacade(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CustomerResponse toResponse(Customer customer) {
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public Customer toEntity(CustomerRequest customerRequest) {
        return modelMapper.map(customerRequest, Customer.class);
    }
}
