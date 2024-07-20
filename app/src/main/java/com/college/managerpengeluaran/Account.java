package com.college.managerpengeluaran;

import java.math.BigDecimal;

public class Account {
    private int accountId;
    private String accountName;
    private String description;
    private BigDecimal initialBalance;
    private String date;

    public Account(int accountId, String accountName, String description, BigDecimal initialBalance) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.description = description;
        this.initialBalance = initialBalance;
    }

    public boolean isEmpty() {
        return accountId == 0
                && (accountName == null || accountName.isEmpty())
                && (description == null || description.isEmpty())
                && initialBalance.compareTo(BigDecimal.ZERO) == 0; // Use BigDecimal.ZERO for comparison
    }

    public Account() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}
