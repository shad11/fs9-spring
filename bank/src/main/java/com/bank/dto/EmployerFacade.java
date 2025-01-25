package com.bank.dto;

import com.bank.entity.Employer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmployerFacade {
    private final ModelMapper modelMapper;

    public EmployerFacade(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public EmployerResponse toResponse(Employer employer) {
        return modelMapper.map(employer, EmployerResponse.class);
    }

    public Employer toEntity(EmployerRequest employerRequest) {
        return modelMapper.map(employerRequest, Employer.class);
    }
}
