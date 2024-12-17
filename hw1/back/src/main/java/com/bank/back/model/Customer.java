package com.bank.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is mandatory")
    private String name;

    @NotBlank(message = "Customer email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 18, message = "Customer should be at least 18 years old")
    private int age;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    public Customer() {
    }

    public Customer(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Customer name is mandatory") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Customer name is mandatory") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Customer email is mandatory") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Customer email is mandatory") String email) {
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
}
