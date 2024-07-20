package com.college.managerpengeluaran;

public class Account {
    private int accountId;
    private String accountName;
    private String description;
    private double initialBalance;
    private String date;

    public Account(int accountId, String accountName, String description, double initialBalance) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.description = description;
        this.initialBalance = initialBalance;
    }

    public boolean isEmpty() {
        return this.accountId == 0 && (this.accountName == null || this.accountName.isEmpty()) &&
                this.initialBalance == 0.0 && (this.description == null || this.description.isEmpty());
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

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }
}
