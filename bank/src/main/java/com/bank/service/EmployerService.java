package com.bank.service;

import com.bank.dto.EmployerFacade;
import com.bank.dto.EmployerRequest;
import com.bank.dto.EmployerResponse;
import com.bank.entity.Employer;
import com.bank.exception.NotFoundException;
import com.bank.repository.EmployerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerService {
    private final EmployerRepository employerRepository;
    private final EmployerFacade employerFacade;

    public EmployerService(EmployerRepository employerRepository, EmployerFacade employerFacade) {
        this.employerRepository = employerRepository;
        this.employerFacade = employerFacade;
    }

    public EmployerResponse save(EmployerRequest employer) {
        Employer savedEmployer = employerRepository.save(employerFacade.toEntity(employer));

        return employerFacade.toResponse(savedEmployer);
    }

    public List<EmployerResponse> getAll() {
        return employerRepository.findAll()
                .stream()
                .map(employerFacade::toResponse)
                .toList();
    }

    public EmployerResponse getById(long id) {
        Employer employer = employerRepository.findById(id).orElseThrow(() -> new NotFoundException("Employer hasn't been found!"));

        return employerFacade.toResponse(employer);
    }

    public EmployerResponse getEmployerByName(String name) {
        Employer employer = employerRepository.findByName(name);

        if (employer == null) {
            return null;
        }

        return employerFacade.toResponse(employer);
    }

    public void delete(long id) {
        employerRepository.deleteById(id);
    }
}
