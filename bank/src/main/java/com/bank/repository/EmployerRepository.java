package com.bank.repository;

import com.bank.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, Long> {
    public Employer findByName(String name);
}