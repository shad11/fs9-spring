package com.bank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer extends AbstractEntity {
    @NotBlank(message = "Customer name is mandatory")
    private String name;

    @NotBlank(message = "Customer email is mandatory")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @Min(value = 18, message = "Customer should be at least 18 years old")
    private int age;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "customer_employer",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "employer_id")
    )
    private Set<Employer> employers = new HashSet<>();

    public Customer() {
    }

    public Customer(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Set<Employer> getEmployers() {
        return employers;
    }

    public void setEmployers(Set<Employer> employers) {
        this.employers = employers;
    }
}