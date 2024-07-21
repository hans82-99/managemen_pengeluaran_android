package com.college.managerpengeluaran;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AssistCat {
    @Query("SELECT * FROM "+modelexpcategory.TABLE_CATEGORY)
    List<modelexpcategory> getAllCat();

    @Insert
    void addto (modelexpcategory modelakun);

    @Update
    void naikver (modelexpcategory modelakun);

    @Delete
    void hapuso (modelexpcategory modelakun);

    @Query("DELETE FROM "+modelexpcategory.TABLE_CATEGORY)
    void deleteAll();
}
