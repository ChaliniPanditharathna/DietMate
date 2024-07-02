package com.example.dietmate.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dietmate.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Retrieve data from arguments
        //if (getArguments() != null) {
            //int age = getArguments().getInt("age");
            int age = 30;
            //double height = getArguments().getDouble("height");
        double height = 167;
        //double weight = getArguments().getDouble("weight");
        double weight = 55;
           // String dietaryPreference = getArguments().getString("dietaryPreference");
          //  String dietaryRestriction = getArguments().getString("dietaryRestriction");
          //  String healthGoal = getArguments().getString("healthGoal");
          //  String preferences = getArguments().getString("preferences");
          //  String gender = getArguments().getString("gender");

            // Calculate BMR
            double bmr = calculateBMR(age, height, weight, "male");

            // You can now use the retrieved data, for example, set it to a TextView
            TextView textView = view.findViewById(R.id.textViewProfileInfo);
            /*textView.setText("Age: " + age + "\nHeight: " + height + "\nWeight: " + weight +
                    "\nDietary Preference: " + dietaryPreference + "\nDietary Restriction: " + dietaryRestriction +
                    "\nHealth Goal: " + healthGoal + "\nPreferences: " + preferences + "\nBMR: " + bmr);*/

           textView.setText("Age: " + age + "\nHeight: " + height + "\nWeight: " + weight + "\nBMR: " + bmr);

            TextView bmi = view.findViewById(R.id.textViewBMI);
            bmi.setText(String.valueOf(bmr));
       // }

        return view;
    }

    private double calculateBMR(int age, double height, double weight, String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            return 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }
    }
}