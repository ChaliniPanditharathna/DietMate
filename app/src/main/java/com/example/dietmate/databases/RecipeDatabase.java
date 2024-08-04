package com.example.dietmate.databases;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.example.dietmate.dao.ProfileDao;
import com.example.dietmate.dao.RecipeDao;
import com.example.dietmate.model.Profile;
import com.example.dietmate.model.Recipe;
import com.example.dietmate.utils.Converters;

@Database(entities = {Recipe.class, Profile.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class RecipeDatabase extends RoomDatabase {
    public abstract RecipeDao recipeDao();
    public abstract ProfileDao profileDao();

    private static volatile RecipeDatabase INSTANCE;

    public static RecipeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RecipeDatabase.class, "recipe_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
