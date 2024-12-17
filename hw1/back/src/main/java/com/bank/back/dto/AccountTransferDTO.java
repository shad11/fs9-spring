package com.bank.back.dto;

public class AccountTransferDTO {
    private Long fromAccountId;
    private Long toAccountId;
    private double amount;

    public AccountTransferDTO(Long fromAccountId, Long toAccountId, double amount) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccount(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccount(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
