package com.example.dietmate.utils;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

public class Converters {
   @TypeConverter
    public static String fromHashMap(HashMap<String, Double> map) {
        Gson gson = new Gson();
        return gson.toJson(map);
    }

    @TypeConverter
    public static HashMap<String, Double> toHashMap(String value) {
        Type type = new TypeToken<HashMap<String, Double>>() {}.getType();
        return new Gson().fromJson(value, type);
    }
}