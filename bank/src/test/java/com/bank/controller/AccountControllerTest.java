package com.bank.controller;

import com.bank.dto.AccountResponse;
import com.bank.enums.Currency;
import com.bank.service.AccountService;
import com.bank.service.WebSocketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @Mock
    private WebSocketService webSocketService;

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
        doNothing().when(webSocketService).sendAccountUpdate(number, accountResponse.getBalance());

        mockMvc.perform(post("/accounts/{number}/deposit", number)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(expectedBalance));

        verify(webSocketService, times(1)).sendAccountUpdate(number, accountResponse.getBalance());
    }

    @Test
    void increaseAccountBadRequest() throws Exception {
        int amount = -50;

        mockMvc.perform(post("/accounts/{number}/deposit", number)
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
        doNothing().when(webSocketService).sendAccountUpdate(number, accountResponse.getBalance());

        mockMvc.perform(post("/accounts/{number}/withdraw", number)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(expectedBalance));

        verify(webSocketService, times(1)).sendAccountUpdate(number, accountResponse.getBalance());
    }

    @Test
    void decreaseAccountBadRequest() throws Exception {
        int amount = -50;

        mockMvc.perform(post("/accounts/{number}/withdraw", number)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount should be greater than 0"));
    }

    @Test
    void transferSuccess() throws Exception {
        AccountResponse targetAccount =  new AccountResponse(2L, "987654321", Currency.USD, 0);
        int amount = 50;

        accountResponse.setBalance(100);

        doNothing().when(webSocketService).sendAccountUpdate(number, accountResponse.getBalance());
        doNothing().when(webSocketService).sendAccountUpdate(targetAccount.getNumber(), targetAccount.getBalance());

        when(accountService.withdraw(number, amount)).thenReturn(accountResponse);
        when(accountService.deposit(targetAccount.getNumber(), amount)).thenReturn(targetAccount);

        mockMvc.perform(post("/accounts/{number}/transfer", number)
                        .contentType("application/json")
                        .content("{\"number\":\"" + targetAccount.getNumber() + "\", \"amount\":" + amount + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transfer successful"));

        verify(webSocketService, times(1)).sendAccountUpdate(number, accountResponse.getBalance());
        verify(webSocketService, times(1)).sendAccountUpdate(targetAccount.getNumber(), targetAccount.getBalance());
    }

    @Test
    void transferBadRequest() throws Exception {
        int amount = -50;

        mockMvc.perform(post("/accounts/{number}/transfer", number)
                        .contentType("application/json")
                        .content("{\"number\":\"987654321\", \"amount\":" + amount + "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount should be greater than 0"));
    }
}
