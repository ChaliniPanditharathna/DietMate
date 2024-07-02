package com.example.dietmate.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dietmate.MainActivity;
import com.example.dietmate.R;
import com.example.dietmate.databinding.ActivityLoginBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "DietMate";

    private ActivityLoginBinding activityLoginBinding;

    @NonNull
    public static Intent createIntent(@NonNull Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            startSignedInActivity(null);
            finish();
        } else {
            showFirebaseUI();
        }
    }

    public void showFirebaseUI() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.logo_dietmate)
                //.setTheme(R.style.Theme_DietMate)
                .build();

        signInLauncher.launch(signInIntent);
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(), this::onSignInResult);

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        handleSignInResponse(result.getResultCode(), response);
    }

    private void handleSignInResponse(int resultCode, @Nullable IdpResponse response) {
        if (resultCode == RESULT_OK) {
            startSignedInActivity(response);
            finish();
        } else {
            if (response == null) {
                showSnackbar(R.string.sign_in_cancelled);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackbar(R.string.no_internet_connection);
                return;
            }

            if (response.getError().getErrorCode() == ErrorCodes.ERROR_USER_DISABLED) {
                showSnackbar(R.string.account_disabled);
                return;
            }

            showSnackbar(R.string.unknown_error);
            Log.e(TAG, "Sign-in error: ", response.getError());
        }
    }

    private void startSignedInActivity(@Nullable IdpResponse response) {
        startActivity(MainActivity.createIntent(this, response));
    }

    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(activityLoginBinding.getRoot(), errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null && getIntent().getExtras() == null) {
            startSignedInActivity(null);
            finish();
        }
    }
}