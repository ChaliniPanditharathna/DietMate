package com.example.dietmate.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.dietmate.utils.Converters;

import java.util.ArrayList;
import java.util.HashMap;

@Entity(tableName = "recipe_table")
@TypeConverters(Converters.class)
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private HashMap<String, Double> totalNutrients;

    @Ignore
    private String image;
    @Ignore
    private String recipeUrl;
    @Ignore
    private ArrayList<String> ingredientLines;

    public Recipe(String title, HashMap<String, Double> totalNutrients) {
        this.title = title;
        this.totalNutrients = totalNutrients;
    }

    @Ignore
    public Recipe(String title, String image, String recipeUrl, ArrayList<String> ingredientLines, HashMap<String, Double> totalNutrients) {
        this.title = title;
        this.image = image;
        this.recipeUrl = recipeUrl;
        this.ingredientLines = ingredientLines;
        this.totalNutrients = totalNutrients;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<String, Double> getTotalNutrients() {
        return totalNutrients;
    }

    public void setTotalNutrients(HashMap<String, Double> totalNutrients) {
        this.totalNutrients = totalNutrients;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public void setRecipeUrl(String recipeUrl) {
        this.recipeUrl = recipeUrl;
    }

    public ArrayList<String> getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(ArrayList<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }
}