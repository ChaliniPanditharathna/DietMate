package com.example.dietmate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dietmate.R;
import com.example.dietmate.model.Recipe;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MealPlannerAdapter extends RecyclerView.Adapter<MealPlannerAdapter.MealPlannerViewHolder> {

    private List<Recipe> recipes;

    public MealPlannerAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public MealPlannerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_planner, parent, false);
        return new MealPlannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MealPlannerViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.title.setText(recipe.getTitle());

        // Convert totalNutrients HashMap to a readable String
        holder.nutrients.setText(formatNutrients(recipe.getTotalNutrients()));

        // Convert timestamp to a readable date string
        holder.date.setText(recipe.getDate());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    private String formatNutrients(HashMap<String, Double> nutrients) {
        StringBuilder nutrientsBuilder = new StringBuilder();
        for (HashMap.Entry<String, Double> entry : nutrients.entrySet()) {
            nutrientsBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("g\n");
        }
        return nutrientsBuilder.toString();
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public static class MealPlannerViewHolder extends RecyclerView.ViewHolder {
        TextView title, nutrients, date;

        public MealPlannerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            nutrients = itemView.findViewById(R.id.nutrients);
            date = itemView.findViewById(R.id.date);
        }
    }
}