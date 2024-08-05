// WeeklyTimetableFragment.java
package com.example.dietmate.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import com.example.dietmate.R;
import com.example.dietmate.dao.RecipeDao;
import com.example.dietmate.databases.RecipeDatabase;
import com.example.dietmate.model.Recipe;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeeklyTimetableFragment extends Fragment {

    private RecipeDatabase db;
    private CalendarView calendarView;
    private RecipeDao recipeDao;

    public WeeklyTimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_timetable, container, false);

        calendarView = view.findViewById(R.id.calendarView);

        db = RecipeDatabase.getDatabase(getContext());
        recipeDao = db.recipeDao();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                new RetrieveRecipesByDateAsyncTask(recipeDao, date).execute();
            }
        });

        return view;
    }

    private class RetrieveRecipesByDateAsyncTask extends AsyncTask<Void, Void, List<Recipe>> {
        private RecipeDao recipeDao;
        private String date;

        private RetrieveRecipesByDateAsyncTask(RecipeDao recipeDao, String date) {
            this.recipeDao = recipeDao;
            this.date = date;
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            return recipeDao.getRecipesByDate(date); // Adjust this method in DAO
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            super.onPostExecute(recipes);
            if (recipes != null && !recipes.isEmpty()) {
                showPopup(recipes);
            } else {
                Toast.makeText(getContext(), "No recipes found for this date", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showPopup(List<Recipe> recipes) {
        StringBuilder details = new StringBuilder();
        for (Recipe recipe : recipes) {
            details.append("Title: ").append(recipe.getTitle()).append("\n")
                    .append("Meal Type: ").append(recipe.getMealType()).append("\n\n");
        }

        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Recipes for Selected Date")
                .setMessage(details.toString())
                .setPositiveButton("OK", null)
                .show();
    }
}