package com.example.dietmate.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        recipeList = new ArrayList<>();
        fetchRecipes();

        return view;
    }

    /*private void fetchRecipes() {
        String url = "https://api.edamam.com/api/recipes/v2?type=public&q=chicken&app_id=f825fd5c&app_key=60ee5f2cb96f07f49d247839253cee8f";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("hits");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("recipe");
                    String title = jsonObject.getString("label");
                    String imageUrl = jsonObject.getString("image");

                    Recipe recipe = new Recipe(title, imageUrl);
                    recipeList.add(recipe);
                }

                RecyclerAdapter adapter = new RecyclerAdapter(getContext(), recipeList);
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            String errorMessage = (error.getMessage() != null) ? error.getMessage() : "An unexpected error occurred";
            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
        });

        requestQueue.add(jsonObjectRequest);
    }*/

    private void fetchRecipes() {
        String url = "https://api.edamam.com/api/recipes/v2?type=public&q=chicken&app_id=f825fd5c&app_key=60ee5f2cb96f07f49d247839253cee8f";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("hits");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i).getJSONObject("recipe");
                    String title = jsonObject.getString("label");
                    String imageUrl = jsonObject.getString("image");

                    Recipe recipe = new Recipe(title, imageUrl);
                    recipeList.add(recipe);
                }

                RecyclerAdapter adapter = new RecyclerAdapter(getContext(), recipeList);
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

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Edamam-Account-User", "f825fd5c");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }


}