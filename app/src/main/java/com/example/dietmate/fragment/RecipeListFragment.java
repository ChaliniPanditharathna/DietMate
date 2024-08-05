package com.example.dietmate.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dietmate.R;
import com.example.dietmate.VolleySingleton;
import com.example.dietmate.adapter.RecyclerAdapter;
import com.example.dietmate.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RecipeListFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    private RequestQueue requestQueue;

    private ArrayList<Recipe> recipeList;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getArguments();
        String prefferedRecipe = bundle != null ? bundle.getString("recipeRelated", "NOTHING") : "NOTHING";
        String dietLabel = bundle != null ? bundle.getString("dietLabel", "NOTHING") : "NOTHING";
        String mealType = bundle != null ? bundle.getString("mealType", "NOTHING") : "NOTHING";
        String cuisineType = bundle != null ? bundle.getString("cuisineType", "NOTHING") : "NOTHING";

        Log.d("prefferedRecipe", prefferedRecipe);
        requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        recipeList = new ArrayList<>();
        fetchRecipes(prefferedRecipe, dietLabel, mealType, cuisineType);

        return view;
    }

    private void fetchRecipes(String prefferedRecipe, String dietLabel, String mealType, String cuisineType) {
        if (prefferedRecipe == null || prefferedRecipe.isEmpty()) {
            prefferedRecipe = "chicken"; // Default recipe
        }
        if (dietLabel == null || dietLabel.isEmpty()) {
            dietLabel = "balanced"; // Default recipe
        }
        if (mealType == null || mealType.isEmpty()) {
            mealType = "brunch"; // Default recipe
        }
        if (cuisineType == null || cuisineType.isEmpty()) {
            cuisineType = "american"; // Default recipe
        }

        String url = "https://api.edamam.com/api/recipes/v2?type=public&q=" + prefferedRecipe + "&diet=" + dietLabel + "&mealType=" + mealType + "&cuisineType=" + cuisineType + "&app_id=f825fd5c&app_key=60ee5f2cb96f07f49d247839253cee8f";

        String finalMealType = mealType;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("hits");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("recipe");
                    String title = jsonObject.getString("label");
                    String imageUrl = jsonObject.getString("image");
                    String recipeUrl = jsonObject.getString("url");
                    JSONArray ingredientsArray = jsonObject.getJSONArray("ingredientLines");
                    ArrayList<String> ingredientLines = new ArrayList<>();
                    for (int j = 0; j < ingredientsArray.length(); j++) {
                        ingredientLines.add(ingredientsArray.getString(j));
                    }

                    JSONObject totalNutrientsObject = jsonObject.getJSONObject("totalNutrients");
                    HashMap<String, Double> totalNutrients = new HashMap<>();

                    // Extracting required nutrients
                    String[] nutrientKeys = {"FAT", "CHOCDF", "PROCNT", "CHOLE", "CA"};
                    for (String key : nutrientKeys) {
                        if (totalNutrientsObject.has(key)) {
                            JSONObject nutrient = totalNutrientsObject.getJSONObject(key);
                            double quantity = nutrient.getDouble("quantity");
                            totalNutrients.put(key, quantity);
                        }
                    }

                    Recipe recipe = new Recipe(title, imageUrl, recipeUrl, ingredientLines, totalNutrients);
                    recipeList.add(recipe);
                }

                RecyclerAdapter adapter = new RecyclerAdapter(getContext(), recipeList, recipe -> {
                    // Navigate to RecipeDetailsFragment
                    RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("title", recipe.getTitle());
                    bundle.putString("image", recipe.getImage());
                    bundle.putString("recipeUrl", recipe.getRecipeUrl());
                    bundle.putStringArrayList("ingredientLines", recipe.getIngredientLines());
                    bundle.putSerializable("totalNutrients", recipe.getTotalNutrients());
                    bundle.putString("mealType", finalMealType);
                    recipeDetailsFragment.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, recipeDetailsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                });
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "JSON parsing error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
            String errorMessage = "An unexpected error occurred";
            if (error.networkResponse != null) {
                errorMessage += ": Status Code " + error.networkResponse.statusCode;
            }
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Edamam-Account-User", "f825fd5c");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }
}