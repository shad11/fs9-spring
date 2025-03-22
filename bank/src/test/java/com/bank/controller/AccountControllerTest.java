package com.bank.controller;

import com.bank.dto.AccountResponse;
import com.bank.enums.Currency;
import com.bank.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;
    private AccountResponse accountResponse;

    private final String number = "1234567890";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();

        accountResponse = new AccountResponse(1L, number, Currency.USD, 0);
    }

    @Test
    void increaseAccountSuccess() throws Exception {
        int amount = 50;
        double expectedBalance = accountResponse.getBalance() + amount;

        accountResponse.setBalance(expectedBalance);

        when(accountService.deposit(number, amount)).thenReturn(accountResponse);

        mockMvc.perform(patch("/accounts/{number}/deposit", number)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(expectedBalance));
    }

    @Test
    void increaseAccountBadRequest() throws Exception {
        int amount = -50;

        mockMvc.perform(patch("/accounts/{number}/deposit", number)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount should be greater than 0"));
    }

    @Test
    void decreaseAccountSuccess() throws Exception {
        double amount = 50.20;
        double expectedBalance;

        accountResponse.setBalance(100.50); // Set initial balance for testing
        expectedBalance = accountResponse.getBalance() - amount;
        accountResponse.setBalance(expectedBalance);

        when(accountService.withdraw(number, amount)).thenReturn(accountResponse);

        mockMvc.perform(patch("/accounts/{number}/withdraw", number)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(expectedBalance));
    }

    @Test
    void decreaseAccountBadRequest() throws Exception {
        int amount = -50;

        mockMvc.perform(patch("/accounts/{number}/withdraw", number)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount should be greater than 0"));
    }

    @Test
    void transferSuccess() throws Exception {
        AccountResponse targetAccount =  new AccountResponse(2L, "987654321", Currency.USD, 0);
        int amount = 50;

        accountResponse.setBalance(100);

        when(accountService.withdraw(number, amount)).thenReturn(accountResponse);
        when(accountService.deposit(targetAccount.getNumber(), amount)).thenReturn(targetAccount);

        mockMvc.perform(patch("/accounts/{number}/transfer", number)
                        .contentType("application/json")
                        .content("{\"number\":\"" + targetAccount.getNumber() + "\", \"amount\":" + amount + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transfer successful"));
    }

    @Test
    void transferBadRequest() throws Exception {
        int amount = -50;

        mockMvc.perform(patch("/accounts/{number}/transfer", number)
                        .contentType("application/json")
                        .content("{\"number\":\"987654321\", \"amount\":" + amount + "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount should be greater than 0"));
    }
}
