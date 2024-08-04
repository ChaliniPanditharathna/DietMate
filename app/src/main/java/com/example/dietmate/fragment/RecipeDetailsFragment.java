package com.example.dietmate.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dietmate.R;
import com.example.dietmate.dao.RecipeDao;
import com.example.dietmate.databases.RecipeDatabase;
import com.example.dietmate.model.Recipe;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RecipeDetailsFragment extends Fragment {

    private RecipeDatabase db;
    private Recipe currentRecipe;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        ImageView detailImage = view.findViewById(R.id.detailImage);
        TextView title = view.findViewById(R.id.detailTitle);
        TextView recipeUrl = view.findViewById(R.id.detailRecipeUrl);
        TextView ingredientLinesTextView = view.findViewById(R.id.ingredientLines);
        PieChart nutrientChart = view.findViewById(R.id.nutrientChart);
        Button saveRecipeButton = view.findViewById(R.id.saveRecipeButton);

        db = RecipeDatabase.getDatabase(getContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            String detailTitle = bundle.getString("title");
            String detailImageString = bundle.getString("image");
            String detailRecipeUrl = bundle.getString("recipeUrl");
            ArrayList<String> ingredientLines = bundle.getStringArrayList("ingredientLines");
            HashMap<String, Double> totalNutrients = (HashMap<String, Double>) bundle.getSerializable("totalNutrients");

            Picasso.get().load(detailImageString).into(detailImage);
            title.setText(detailTitle);
            recipeUrl.setText(detailRecipeUrl);

            // Display ingredient lines
            StringBuilder ingredientsBuilder = new StringBuilder();
            if (ingredientLines != null) {
                for (String line : ingredientLines) {
                    ingredientsBuilder.append(line).append("\n");
                }
            }
            ingredientLinesTextView.setText(ingredientsBuilder.toString());

            // Display specific nutrients in the PieChart
            ArrayList<PieEntry> entries = new ArrayList<>();
            if (totalNutrients != null) {
                // Define the keys for the required nutrients
                String[] requiredNutrients = {"FAT", "CHOCDF", "PROCNT", "CHOLE", "CA"};
                HashMap<String, String> nutrientLabels = new HashMap<>();
                nutrientLabels.put("FAT", "Fat");
                nutrientLabels.put("CHOCDF", "Carbs");
                nutrientLabels.put("PROCNT", "Protein");
                nutrientLabels.put("CHOLE", "Cholesterol");
                nutrientLabels.put("CA", "Calcium");

                for (String nutrientKey : requiredNutrients) {
                    if (totalNutrients.containsKey(nutrientKey)) {
                        double quantity = totalNutrients.get(nutrientKey);
                        entries.add(new PieEntry((float) quantity, nutrientLabels.get(nutrientKey) + " " + quantity + "g"));
                    }
                }
            }

            PieDataSet dataSet = new PieDataSet(entries, "Nutrients");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            PieData pieData = new PieData(dataSet);
            pieData.setValueTextSize(12f);
            pieData.setValueFormatter(new PercentFormatter(nutrientChart));
            nutrientChart.setData(pieData);
            nutrientChart.invalidate(); // refresh chart

            // Customize chart description
            Description description = new Description();
            description.setText("Total Nutrients");
            description.setTextSize(16f); // Increase font size
            nutrientChart.setDescription(description);

            // Initialize the current recipe with current date
            currentRecipe = new Recipe(detailTitle, detailImageString, detailRecipeUrl, ingredientLines, totalNutrients);
        }

        saveRecipeButton.setOnClickListener(v -> {
            if (currentRecipe != null) {
                // Save current date
                currentRecipe.setDate(new Date().getTime());
                new InsertRecipeAsyncTask(db.recipeDao()).execute(currentRecipe);
                Toast.makeText(getContext(), "Recipe saved!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private static class InsertRecipeAsyncTask extends AsyncTask<Recipe, Void, Void> {
        private RecipeDao recipeDao;

        private InsertRecipeAsyncTask(RecipeDao recipeDao) {
            this.recipeDao = recipeDao;
        }

        @Override
        protected Void doInBackground(Recipe... recipes) {
            recipeDao.insert(recipes[0]);
            return null;
        }
    }
}