package com.example.dietmate.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dietmate.R;
import com.example.dietmate.adapter.ProfileOptionsAdapter;
import com.example.dietmate.database.DietMateDatabase;
import com.example.dietmate.databinding.FragmentProfileBinding;
import com.example.dietmate.fragment.RecipeRequestFragment;
import com.example.dietmate.fragment.UpdateProfileFragment;
import com.example.dietmate.login.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private static final List<Integer> IMAGES_LIST = List.of(R.drawable.update,R.drawable.log_out, R.drawable.delete_account);
    private static final List<String> LABELS_LIST = List.of("Update Profile","Sign Out", "Delete Account");

    private static final String TAG = "DietMate";

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivity(LoginActivity.createIntent(getContext()));
        }

        populateProfile();

        ProfileOptionsAdapter profileOptionsAdapter = new ProfileOptionsAdapter(IMAGES_LIST, LABELS_LIST, position -> {
            switch (position) {
                case 0:
                    /*Intent intent = new Intent(getActivity(), ProfileUpdateActivity.class);
                    startActivity(intent);*/
                    Fragment fragment = new UpdateProfileFragment();
                    replaceFragment(fragment);
                    break;
                case 1:
                    signOut();
                    break;
                case 2:
                    deleteAccountClicked();
                    break;
            }
        });
        binding.recyclerViewProfile.setAdapter(profileOptionsAdapter);
        binding.recyclerViewProfile.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void populateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        binding.userEmail.setText(TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());
        binding.userDisplayName.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? "No display name" : user.getDisplayName());
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(getContext())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(LoginActivity.createIntent(getContext()));
                    } else {
                        Log.w(TAG, "signOut:failure", task.getException());
                        showSnackbar(R.string.sign_out_failed);
                    }
                });
    }

    public void deleteAccountClicked() {
        new MaterialAlertDialogBuilder(getContext())
                .setMessage("Are you sure you want to delete this account?")
                .setPositiveButton("Yes", (dialogInterface, i) -> deleteAccount())
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteAccount() {
        AuthUI.getInstance()
                .delete(getContext())
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Executors.newSingleThreadExecutor().execute(() ->
                                DietMateDatabase.getInstance(getContext()).clearAllTables());
                        startActivity(LoginActivity.createIntent(getContext()));
                    } else {
                        showSnackbar(R.string.delete_account_failed);
                    }
                });
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(binding.getRoot(), errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}