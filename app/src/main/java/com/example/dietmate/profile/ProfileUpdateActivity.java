package com.example.dietmate.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dietmate.R;
import com.example.dietmate.databinding.ActivityProfileUpdateBinding;

public class ProfileUpdateActivity extends AppCompatActivity {

    private ActivityProfileUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile_update);
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

                // For demonstration purposes, show a Toast message
                Toast.makeText(ProfileUpdateActivity.this, "Updated Profile:\nAge: " + age + "\nHeight: " + height +
                        "\nWeight: " + weight + "\nDietary Preference: " + dietaryPreference + "\nDietary Restriction: " + dietaryRestriction +
                        "\nHealth Goal: " + healthGoal + "\nPreferences: " + preferences, Toast.LENGTH_LONG).show();

                // Save the data to shared preferences or a database here
            }
        });
    }
}