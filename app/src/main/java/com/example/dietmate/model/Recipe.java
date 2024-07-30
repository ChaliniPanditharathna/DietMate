package com.example.dietmate.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Recipe {
    private String title;
    private String image;
    private String recipeUrl;
    private ArrayList<String> ingredientLines;
    private HashMap<String, Double> totalNutrients;

    public Recipe(String title, String image, String recipeUrl, ArrayList<String> ingredientLines, HashMap<String, Double> totalNutrients) {
        this.title = title;
        this.image = image;
        this.recipeUrl = recipeUrl;
        this.ingredientLines = ingredientLines;
        this.totalNutrients = totalNutrients;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public HashMap<String, Double> getTotalNutrients() {
        return totalNutrients;
    }

    public void setTotalNutrients(HashMap<String, Double> totalNutrients) {
        this.totalNutrients = totalNutrients;
    }
}
