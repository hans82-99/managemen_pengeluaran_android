package com.college.managerpengeluaran;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface AssistAkun {
    @Query("SELECT * FROM "+modelakun.TABLE_NAME)
    List<modelakun> getAkun();

    @Insert
    void addto (modelakun modelakun);

    @Update
    void naikver (modelakun modelakun);

    @Delete
    void hapuso (modelakun modelakun);

    @Query("DELETE FROM "+modelakun.TABLE_NAME)
    void deleteAll();
}
