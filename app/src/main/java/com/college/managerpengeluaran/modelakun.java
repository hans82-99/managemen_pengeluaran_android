package com.college.managerpengeluaran;

import java.math.BigDecimal;
import java.util.Date;

public class modelakun {
    String account_name;
    String description;
    String initial_balance;
    String date;

    public modelakun(String account_name, String description, String initial_balance, String date) {
        this.account_name = account_name;
        this.description = description;
        this.initial_balance = initial_balance;
        this.date = date;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInitial_balance() {
        return initial_balance;
    }

    public void setInitial_balance(String initial_balance) {
        this.initial_balance = initial_balance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
