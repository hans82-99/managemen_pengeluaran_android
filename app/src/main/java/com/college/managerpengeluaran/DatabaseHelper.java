package com.college.managerpengeluaran;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = modelakun.class, version = 1, exportSchema = false)
public abstract class DatabaseHelper extends RoomDatabase {
    public static final String DB_NAME = "ExpenseDB";
    public static DatabaseHelper instance;

    public static synchronized DatabaseHelper getDB(Context context){
        if (instance == null) {
            instance = Room.databaseBuilder(context, DatabaseHelper.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract AssistAkun AssistAkun();
}
