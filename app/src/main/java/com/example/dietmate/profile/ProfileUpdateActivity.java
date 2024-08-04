package com.example.dietmate.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dietmate.R;
import com.example.dietmate.databinding.ActivityProfileUpdateBinding;
import com.example.dietmate.dao.ProfileDao;
import com.example.dietmate.databases.RecipeDatabase;
import com.example.dietmate.fragment.HomeFragment;
import com.example.dietmate.model.Profile;

public class ProfileUpdateActivity extends AppCompatActivity {

    private ActivityProfileUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Populate the spinners
        ArrayAdapter<CharSequence> dietaryPreferencesAdapter = ArrayAdapter.createFromResource(this,
                R.array.dietary_preferences_array, android.R.layout.simple_spinner_item);
        dietaryPreferencesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDietaryPreferences.setAdapter(dietaryPreferencesAdapter);

        ArrayAdapter<CharSequence> dietaryRestrictionsAdapter = ArrayAdapter.createFromResource(this,
                R.array.dietary_restrictions_array, android.R.layout.simple_spinner_item);
        dietaryRestrictionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDietaryRestrictions.setAdapter(dietaryRestrictionsAdapter);

        ArrayAdapter<CharSequence> healthGoalsAdapter = ArrayAdapter.createFromResource(this,
                R.array.health_goals_array, android.R.layout.simple_spinner_item);
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
                String preferences = binding.editTextPreferences.getText().toString();

                // Create a Profile object to save or update
                Profile profile = new Profile();
                profile.age = Integer.parseInt(age);
                profile.height = Double.parseDouble(height);
                profile.weight = Double.parseDouble(weight);
                profile.dietaryPreference = dietaryPreference;
                profile.dietaryRestriction = dietaryRestriction;
                profile.healthGoal = healthGoal;
                profile.preferences = preferences;

                // You might need to specify an ID for updates, otherwise it will insert a new record
                // For example, let's set a static ID of 1 for this demo; adjust as needed
                profile.id = 1; // Change this as per your requirement

                // Perform the database operation on a background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RecipeDatabase db = RecipeDatabase.getDatabase(ProfileUpdateActivity.this);
                        ProfileDao profileDao = db.profileDao();

                        // Insert or update the profile in the database
                        profileDao.insert(profile);

                        // Show a confirmation Toast message on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ProfileUpdateActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();

                // Create a bundle and pass data to the fragment
              /*  Bundle bundle = new Bundle();
                bundle.putInt("age", Integer.parseInt(age));
                bundle.putDouble("height", Double.parseDouble(height));
                bundle.putDouble("weight", Double.parseDouble(weight));
                bundle.putString("dietaryPreference", dietaryPreference);
                bundle.putString("dietaryRestriction", dietaryRestriction);
                bundle.putString("healthGoal", healthGoal);
                bundle.putString("preferences", preferences);

                // Create a new instance of HomeFragment and set arguments
                Fragment fragment = new HomeFragment();
                fragment.setArguments(bundle);

                // Replace the current fragment with HomeFragment
                replaceFragment(fragment);*/
            }

            /*private void replaceFragment(Fragment fragment) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }*/
        });
    }
}