package com.college.managerpengeluaran;

import java.math.BigDecimal;
import java.util.Date;

public class modelekspense {
    String expense_title;
    BigDecimal expense_amount;
    Date date;
    String datetime;
    String description;
    String exp_image_desc;
    String quantity;
    String exp_payment_method;
    int user_id;

    public String getExpense_title() {
        return expense_title;
    }
    public void setExpense_title(String expense_title) {
        this.expense_title = expense_title;
    }

    public BigDecimal getExpense_amount() {
        return expense_amount;
    }
    public void setExpense_amount(BigDecimal expense_amount) {
        this.expense_amount = expense_amount;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getDatetime() {
        return datetime;
    }
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getExp_image_desc() {
        return exp_image_desc;
    }
    public void setExp_image_desc(String exp_image_desc) {
        this.exp_image_desc = exp_image_desc;
    }

    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExp_payment_method() {
        return exp_payment_method;
    }
    public void setExp_payment_method(String exp_payment_method) {
        this.exp_payment_method = exp_payment_method;
    }

    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
