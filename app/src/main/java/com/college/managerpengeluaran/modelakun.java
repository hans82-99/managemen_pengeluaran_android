package com.college.managerpengeluaran;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.Date;

@Entity(tableName = "modelakun")
public class modelakun {
    public static final String TABLE_NAME = "modelakun";

    @PrimaryKey
    @ColumnInfo(name = "account_id")
    int account_id;

    @ColumnInfo(name = "account_name")
    String account_name;

    @ColumnInfo(name = "description")
    String description;

    @ColumnInfo(name = "initial_balance")
    String initial_balance;

    @ColumnInfo(name = "date")
    String date;

    public modelakun(String account_name, String description, String initial_balance, String date) {
        this.account_name = account_name;
        this.description = description;
        this.initial_balance = initial_balance;
        this.date = date;
    }

    public modelakun(int id, String accountName, String description, String initial_balance, String date) {
        this.account_id = id;
        this.account_name = accountName;
        this.description = description;
        this.initial_balance = initial_balance;
        this.date = date;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
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
