package com.college.managerpengeluaran;

import java.math.BigDecimal;
import java.util.Date;

public class modelekspense extends Transaction {
    String expense_id;
    String quantity;

    // Getters and Setters
    public String getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(String expense_id) {
        this.expense_id = expense_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
