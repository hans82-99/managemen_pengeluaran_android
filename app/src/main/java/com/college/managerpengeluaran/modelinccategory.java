package com.college.managerpengeluaran;

public class modelinccategory {
    String income_category_name;
    int income_category_id;

    public modelinccategory(String income_category_name, int income_category_id) {
        this.income_category_name = income_category_name;
        this.income_category_id = income_category_id;
    }

    public String getIncome_category_name() {
        return income_category_name;
    }
    public int getIncome_category_id() {
        return income_category_id;
    }

    public void setIncome_category_name(String income_category_name) {
        this.income_category_name = income_category_name;
    }
    public void setIncome_category_id(int income_category_id) {
        this.income_category_id = income_category_id;
    }
}
