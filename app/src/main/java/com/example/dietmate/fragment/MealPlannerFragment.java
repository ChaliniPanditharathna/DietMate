package com.example.dietmate.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.dietmate.R;
import com.example.dietmate.adapter.MealPlannerAdapter;
import com.example.dietmate.dao.RecipeDao;
import com.example.dietmate.databases.RecipeDatabase;
import com.example.dietmate.model.Recipe;
import java.util.List;

public class MealPlannerFragment extends Fragment {

    private RecipeDatabase db;
    private RecyclerView recyclerView;
    private MealPlannerAdapter adapter;

    public MealPlannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_planner, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = RecipeDatabase.getDatabase(getContext());

        new RetrieveRecipesAsyncTask(db.recipeDao()).execute();

        return view;
    }

    private class RetrieveRecipesAsyncTask extends AsyncTask<Void, Void, List<Recipe>> {
        private RecipeDao recipeDao;

        private RetrieveRecipesAsyncTask(RecipeDao recipeDao) {
            this.recipeDao = recipeDao;
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            return recipeDao.getAllRecipes();
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            super.onPostExecute(recipes);
            adapter = new MealPlannerAdapter(recipes);
            recyclerView.setAdapter(adapter);
        }
    }
}