package com.bank.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Employer extends AbstractEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @ManyToMany(mappedBy = "employers")
    @JsonIgnore
    private Set<Customer> customers = new HashSet<>();
}
