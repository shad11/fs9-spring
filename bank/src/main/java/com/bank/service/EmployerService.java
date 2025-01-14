package com.bank.service;

import com.bank.exception.CustomerException;
import com.bank.entity.Employer;
import com.bank.repository.EmployerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerService {
    private final EmployerRepository employerRepository;

    public EmployerService(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    public Employer save(Employer employer) {
        return employerRepository.save(employer);
    }

    public List<Employer> getAll() {
        return employerRepository.findAll();
    }

    public Employer getById(long id) {
        return employerRepository.findById(id).orElseThrow(() -> new CustomerException("Employer hasn't been found!"));
    }

    public Employer getEmployerByName(String name) {
        return employerRepository.findByName(name);
    }

    public void delete(long id) {
        employerRepository.deleteById(id);
    }
}
