package com.bank.dto;

import com.bank.entity.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountFacade {
    private final ModelMapper modelMapper;

    public AccountFacade(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AccountResponse toResponse(Account account) {
        return modelMapper.map(account, AccountResponse.class);
    }

    public Account toEntity(AccountRequest accountRequest) {
        return modelMapper.map(accountRequest, Account.class);
    }
}
