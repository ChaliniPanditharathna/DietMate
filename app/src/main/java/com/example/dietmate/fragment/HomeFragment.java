package com.example.dietmate.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.dietmate.R;
import com.example.dietmate.dao.ProfileDao;
import com.example.dietmate.databases.RecipeDatabase;
import com.example.dietmate.model.Profile;
import com.example.dietmate.model.Recipe;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private RecipeDatabase recipeDatabase;
    private ProfileDao profileDao;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecipeDatabase
        recipeDatabase = RecipeDatabase.getDatabase(requireContext());
        profileDao = recipeDatabase.profileDao();

        // Retrieve and display nutrient data
        new RetrieveDataTask().execute("2024-8-4"); // Example date, adjust as needed

        // Retrieve profile data
        new RetrieveProfileTask().execute(1); // Example profile ID, adjust as needed

        // Set up button to switch fragments
        Button buttonFindRecipe = view.findViewById(R.id.buttonFindRecipe);
        buttonFindRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RecipeRequestFragment();
                replaceFragment(fragment);
            }
        });

        return view;
    }

    private double calculateBMR(int age, double height, double weight, String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            return 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }
    }

    private class RetrieveDataTask extends AsyncTask<String, Void, Map<String, Double>> {

        @Override
        protected Map<String, Double> doInBackground(String... dates) {
            String date = dates[0];
            List<Recipe> recipes = recipeDatabase.recipeDao().getRecipesByDate(date);
            Map<String, Double> nutrientTotals = new HashMap<>();

            for (Recipe recipe : recipes) {
                Map<String, Double> nutrients = recipe.getTotalNutrients();
                for (Map.Entry<String, Double> entry : nutrients.entrySet()) {
                    nutrientTotals.put(entry.getKey(), nutrientTotals.getOrDefault(entry.getKey(), 0.0) + entry.getValue());
                }
            }

            return nutrientTotals;
        }

        @Override
        protected void onPostExecute(Map<String, Double> nutrientTotals) {
            super.onPostExecute(nutrientTotals);
            displayNutrientGraph(nutrientTotals);
        }
    }

    private class RetrieveProfileTask extends AsyncTask<Integer, Void, Profile> {

        @Override
        protected Profile doInBackground(Integer... ids) {
            int id = ids[0];
            return profileDao.getProfileById(id);
        }

        @Override
        protected void onPostExecute(Profile profile) {
            super.onPostExecute(profile);
            if (profile != null) {
                double bmr = calculateBMR(profile.age, profile.height, profile.weight, "male");
                TextView textView = getView().findViewById(R.id.textViewProfileInfo);
                textView.setText("Age: " + profile.age + "\nHeight: " + profile.height + "\nWeight: " + profile.weight + "\nBMR: " + bmr);
            } else {
                // Handle the case where the profile is not found
                TextView textView = getView().findViewById(R.id.textViewProfileInfo);
                textView.setText("Profile not found.");
            }
        }
    }

    private void displayNutrientGraph(Map<String, Double> nutrientTotals) {
        BarChart barChart = getView().findViewById(R.id.barChart);

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int i = 0;

        for (Map.Entry<String, Double> entry : nutrientTotals.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue().floatValue()));
            labels.add(entry.getKey());
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Nutrients");
        dataSet.setValueTextSize(10f); // Set the size of the value text
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        });

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setLabelCount(labels.size()); // Set the number of x-axis labels
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false); // Disable the right y-axis
        barChart.getDescription().setEnabled(false); // Disable the description text
        barChart.setFitBars(true); // Make the bars fit the width of the chart
        barChart.invalidate(); // Refresh the chart
    }

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}