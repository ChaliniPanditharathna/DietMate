package com.example.dietmate.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.dietmate.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insert(Recipe recipe);

    @Query("SELECT * FROM recipe_table")
    List<Recipe> getAllRecipes();
}