package com.example.fitcarehub;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.fitcarehub.databinding.ActivityProfileSetupBinding; // Make sure this import matches your layout file name
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileSetupActivity extends AppCompatActivity {
    private int backPressCounter = 0;
    private long lastBackPressTime = 0;
    private ActivityProfileSetupBinding binding; // Update for view binding
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileSetupBinding.inflate(getLayoutInflater()); // Initialize binding
        setContentView(binding.getRoot());

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("Users");

        binding.continueBtn.setOnClickListener(view -> {
            String name = binding.name.getText().toString().trim();
            String surname = binding.surname.getText().toString().trim();

            if (validateInputs(name, surname)) {
                saveUserToFirebase(name, surname); // Save to Firebase and navigate
            }
        });

        binding.goToLogin.setOnClickListener(view -> navigateToLogin());
    }

    private void saveUserToFirebase(String name, String surname) {
        String userId = reference.push().getKey();
        if (userId == null) return;
        User user = new User(name, surname);
        reference.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    showToast("Profile saved successfully");
                    navigateToMainActivity(); // Navigate to MainActivity after saving
                })
                .addOnFailureListener(e -> showToast("Failed to save profile"));
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(ProfileSetupActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private boolean validateInputs(String name, String surname) {
        boolean isValid = true;
        if (TextUtils.isEmpty(name)) {
            binding.name.setBackgroundResource(R.drawable.edittext_error);
            showToast("Please enter your name");
            isValid = false;
        }
        if (TextUtils.isEmpty(surname)) {
            binding.surname.setBackgroundResource(R.drawable.edittext_error);
            showToast("Please enter your surname");
            isValid = false;
        }
        return isValid;
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ProfileSetupActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastBackPressTime < 2000) {
            if (++backPressCounter >= 2) {
                finishAffinity();
                return;
            }
        } else {
            backPressCounter = 1;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        lastBackPressTime = System.currentTimeMillis();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static class User {
        public String name;
        public String surname;

        public User() {
            // Default constructor required for Firebase
        }

        public User(String name, String surname) {
            this.name = name;
            this.surname = surname;
        }
    }
}
