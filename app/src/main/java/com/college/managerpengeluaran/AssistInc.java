package com.college.managerpengeluaran;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AssistInc {
    @Query("SELECT * FROM "+modelinccategory.TABLE_CATEGORY_INCOME)
    List<modelinccategory> getAllCat();

    @Insert
    void addto (modelinccategory modelakun);

    @Update
    void naikver (modelinccategory modelakun);

    @Delete
    void hapuso (modelinccategory modelakun);

    @Query("DELETE FROM "+modelinccategory.TABLE_CATEGORY_INCOME)
    void deleteAll();
}
