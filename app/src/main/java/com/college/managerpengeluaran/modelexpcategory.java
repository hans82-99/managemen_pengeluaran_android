package com.college.managerpengeluaran;

public class modelexpcategory {
    String expense_category_name;
    int id_expense;

    public modelexpcategory(String expense_category_name, int id_expense) {
        this.expense_category_name = expense_category_name;
        this.id_expense = id_expense;
    }

    public String getExpense_category_name() {
        return expense_category_name;
    }

    public void setExpense_category_name(String expense_category_name) {
        this.expense_category_name = expense_category_name;
    }

    public int getId_expense() {
        return id_expense;
    }

    public void setId_expense(int id_expense) {
        this.id_expense = id_expense;
    }
}
