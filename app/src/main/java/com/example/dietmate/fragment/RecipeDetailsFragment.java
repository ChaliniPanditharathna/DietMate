package com.example.dietmate.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dietmate.R;
import com.squareup.picasso.Picasso;

public class RecipeDetailsFragment extends Fragment {

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            String detailTitle = bundle.getString("title");
            String detailImageString = bundle.getString("image");

            Picasso.get().load(detailImageString).into(detailImage);
            title.setText(detailTitle);
        }

        return view;
    }
}