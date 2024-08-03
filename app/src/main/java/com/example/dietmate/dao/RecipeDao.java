package com.example.dietmate.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.dietmate.model.Recipe;

@Dao
public interface RecipeDao {
    @Insert
    void insert(Recipe recipe);
}