package com.example.dietmate.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dietmate.model.Profile;

@Dao
public interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Profile profile);

    @Update
    void update(Profile profile);

    @Query("SELECT * FROM profile WHERE id = :id")
    Profile getProfileById(int id);

    // Other query methods can be added here
}