package com.example.dietmate.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

public abstract class DietMateDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "dietmate_db";
    private static DietMateDatabase instance;

    public static synchronized DietMateDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            DietMateDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
