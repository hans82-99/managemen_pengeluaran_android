package com.college.managerpengeluaran;

public class Account {
    private int accountId;
    private String accountName;
    private String description;
    private double initialBalance;

    public Account(int accountId, String accountName, String description, double initialBalance) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.description = description;
        this.initialBalance = initialBalance;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getDescription() {
        return description;
    }

    public double getInitialBalance() {
        return initialBalance;
    }
}
