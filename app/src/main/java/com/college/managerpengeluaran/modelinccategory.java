package com.college.managerpengeluaran;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "modelinccategory")
public class modelinccategory {
    public static final String TABLE_CATEGORY_INCOME = "modelinccategory";

    @PrimaryKey
    @ColumnInfo(name = "income_category_id")
    int income_category_id;

    @ColumnInfo(name = "income_category_name")
    String income_category_name;

    public modelinccategory(String income_category_name, int income_category_id) {
        this.income_category_name = income_category_name;
        this.income_category_id = income_category_id;
    }

    public modelinccategory() {

    }

    public modelinccategory(int nampungid, String valcat) {
        this.income_category_id = nampungid;
        this.income_category_name = valcat;
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
