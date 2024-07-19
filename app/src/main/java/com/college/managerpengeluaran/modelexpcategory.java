package com.college.managerpengeluaran;

public class modelexpcategory {
    String expense_category_name;
    int id_expense;

    public modelexpcategory(String expense_category_name, int id_expense) {
        this.expense_category_name = expense_category_name;
        this.id_expense = id_expense;
    }

    public modelexpcategory(int i) {
        this.id_expense = i;
    }

    public modelexpcategory() {

    }

    public modelexpcategory(int nampungid, String valcat) {
        this.id_expense = nampungid;
        this.expense_category_name = valcat;
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
