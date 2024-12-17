package com.bank.back.repository;

import com.bank.back.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByNumber(String number);
    List<Account> findByCustomerId(Long customerId);
}
