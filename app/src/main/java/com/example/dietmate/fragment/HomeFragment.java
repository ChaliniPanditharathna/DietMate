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
import com.example.dietmate.databases.RecipeDatabase;
import com.example.dietmate.model.Recipe;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private RecipeDatabase recipeDatabase;

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

        // Retrieve and display nutrient data
        new RetrieveDataTask().execute("2024-08-03"); // Example date, adjust as needed

        // Example BMR calculation
        int age = 30;
        double height = 167;
        double weight = 55;
        String gender = "male";
        double bmr = calculateBMR(age, height, weight, gender);

        // Display BMR and profile info
        TextView textView = view.findViewById(R.id.textViewProfileInfo);
        textView.setText("Age: " + age + "\nHeight: " + height + "\nWeight: " + weight + "\nBMR: " + bmr);

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

    /*private void displayNutrientGraph(Map<String, Double> nutrientTotals) {
        BarChart barChart = getView().findViewById(R.id.barChart);

        List<BarEntry> entries = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Double> entry : nutrientTotals.entrySet()) {
            entries.add(new BarEntry(i++, entry.getValue().floatValue()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Nutrients");
        BarData barData = new BarData(dataSet);

        barChart.setData(barData);
        barChart.invalidate(); // refresh the chart
    }*/

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