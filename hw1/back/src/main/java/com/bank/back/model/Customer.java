package com.bank.back.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer {
    private long id = IdGenerator.generateCustomerId();

    private String name;

    private String email;

    private int age;

    @JsonManagedReference
    private List<Account> accounts = new ArrayList<>();

    public Customer(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Customer customer))
            return false;

        return age == customer.getAge() &&
                customer.getId() == id &&
                customer.getName().equals(name) &&
                customer.getEmail().equals(email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }

    @Override
    public String toString() {
        return "Customer { " +
                "name: '" + name + '\'' +
                ", email: '" + email + '\'' +
                ", age: " + age +
                ", accounts: " + accounts +
                " }";
    }
}
