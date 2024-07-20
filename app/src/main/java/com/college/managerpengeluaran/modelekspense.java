package com.college.managerpengeluaran;

import java.math.BigDecimal;
import java.util.Date;

public class modelekspense extends Transaction {
    String expense_id;
    String quantity;
/*
    public modelekspense(String expense_title, String expense_amount, String date, String datetime, String description, String exp_image_desc, String exp_payment_method, String user_id, int expense_category_id, String quantity) {
        super();
        this.title = expense_title;
        this.amount = expense_amount;
        this.date = date;
        this.datetime = datetime;
        this.description = description;
        this.imageDesc = exp_image_desc;
        this.paymentMethod = exp_payment_method;
        this.user_id = user_id;
        this.category = expense_category_id;
    }
 */

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
