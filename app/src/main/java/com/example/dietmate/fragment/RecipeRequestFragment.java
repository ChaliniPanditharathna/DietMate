package com.example.dietmate.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.dietmate.R;


public class RecipeRequestFragment extends Fragment {


    public RecipeRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_request, container, false);

        ArrayAdapter<CharSequence> dietLabelsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.diet_label_array, android.R.layout.simple_spinner_item);
        dietLabelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerDietLabels = view.findViewById(R.id.spinnerDietLabels);
        spinnerDietLabels.setAdapter(dietLabelsAdapter);

        ArrayAdapter<CharSequence> mealTypeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.meal_type_array, android.R.layout.simple_spinner_item);
        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerMealType = view.findViewById(R.id.spinnerMealTypes);
        spinnerMealType.setAdapter(mealTypeAdapter);

        ArrayAdapter<CharSequence> cuisineTypeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.cuisine_type_array, android.R.layout.simple_spinner_item);
        cuisineTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerCuisineType = view.findViewById(R.id.spinnerCuisineType);
        spinnerCuisineType.setAdapter(cuisineTypeAdapter);

        Button buttonRecipe = view.findViewById(R.id.buttonPickRecipe);
        buttonRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle result = new Bundle();
                //result.putString("BREAKDOWNTYPE", breakdownType);
                //result.putString("CURRENTLOCATION",address);

                Fragment fragment = new RecipeListFragment();
                fragment.setArguments(result);

                replaceFragment(fragment);
            }
        });

        return view;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}