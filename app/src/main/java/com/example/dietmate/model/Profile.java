package com.example.dietmate.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "profile")
public class Profile {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int age;
    public double height;
    public double weight;
    public String dietaryPreference;
    public String dietaryRestriction;
    public String healthGoal;
    public String preferences;

    public Profile() {
    }

    public Profile(int id, int age, double height, double weight, String dietaryPreference, String dietaryRestriction, String healthGoal, String preferences) {
        this.id = id;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.dietaryPreference = dietaryPreference;
        this.dietaryRestriction = dietaryRestriction;
        this.healthGoal = healthGoal;
        this.preferences = preferences;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getDietaryPreference() {
        return dietaryPreference;
    }

    public void setDietaryPreference(String dietaryPreference) {
        this.dietaryPreference = dietaryPreference;
    }

    public String getDietaryRestriction() {
        return dietaryRestriction;
    }

    public void setDietaryRestriction(String dietaryRestriction) {
        this.dietaryRestriction = dietaryRestriction;
    }

    public String getHealthGoal() {
        return healthGoal;
    }

    public void setHealthGoal(String healthGoal) {
        this.healthGoal = healthGoal;
    }

    public String getPreferences() {
        return preferences;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
}