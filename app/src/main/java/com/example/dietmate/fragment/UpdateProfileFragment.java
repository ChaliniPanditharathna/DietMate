package com.example.dietmate.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dietmate.R;
import com.example.dietmate.dao.ProfileDao;
import com.example.dietmate.databases.RecipeDatabase;
import com.example.dietmate.databinding.FragmentUpdateProfileBinding;
import com.example.dietmate.model.Profile;

public class UpdateProfileFragment extends Fragment {

    private FragmentUpdateProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false);

        // Populate the spinners
        ArrayAdapter<CharSequence> dietaryPreferencesAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.dietary_preferences_array,
                android.R.layout.simple_spinner_item);
        dietaryPreferencesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDietaryPreferences.setAdapter(dietaryPreferencesAdapter);

        ArrayAdapter<CharSequence> dietaryRestrictionsAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.dietary_restrictions_array,
                android.R.layout.simple_spinner_item);
        dietaryRestrictionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDietaryRestrictions.setAdapter(dietaryRestrictionsAdapter);

        ArrayAdapter<CharSequence> healthGoalsAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.health_goals_array,
                android.R.layout.simple_spinner_item);
        healthGoalsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerHealthGoals.setAdapter(healthGoalsAdapter);

        binding.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the update button click
                String age = binding.editTextAge.getText().toString();
                String height = binding.editTextHeight.getText().toString();
                String weight = binding.editTextWeight.getText().toString();
                String dietaryPreference = binding.spinnerDietaryPreferences.getSelectedItem().toString();
                String dietaryRestriction = binding.spinnerDietaryRestrictions.getSelectedItem().toString();
                String healthGoal = binding.spinnerHealthGoals.getSelectedItem().toString();
                //String preferences = binding.editTextPreferences.getText().toString();

                // Create a Profile object to save or update
                Profile profile = new Profile();
                profile.age = Integer.parseInt(age);
                profile.height = Double.parseDouble(height);
                profile.weight = Double.parseDouble(weight);
                profile.dietaryPreference = dietaryPreference;
                profile.dietaryRestriction = dietaryRestriction;
                profile.healthGoal = healthGoal;
                //profile.preferences = preferences;

                // You might need to specify an ID for updates, otherwise it will insert a new record
                // For example, let's set a static ID of 1 for this demo; adjust as needed
                profile.id = 1; // Change this as per your requirement

                // Perform the database operation on a background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RecipeDatabase db = RecipeDatabase.getDatabase(getContext());
                        ProfileDao profileDao = db.profileDao();

                        // Insert or update the profile in the database
                        profileDao.insert(profile);

                        // Show a confirmation Toast message on the main thread
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                                Fragment fragment = new HomeFragment();
                                replaceFragment(fragment);
                            }
                        });
                    }
                }).start();
            }
        });

        return binding.getRoot();
    }

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}