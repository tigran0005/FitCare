package com.example.fitcarehub;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileSetupActivity extends AppCompatActivity {
    private int backPressCounter = 0;
    private long lastBackPressTime = 0;
    private EditText nameEditText, surnameEditText;
    private Button continueButton;
    private TextView goToLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        nameEditText = findViewById(R.id.name);
        surnameEditText = findViewById(R.id.surname);

        continueButton = findViewById(R.id.continueBtn);

        goToLoginTextView = findViewById(R.id.goToLogin);

        continueButton.setOnClickListener(view -> {
            // Reset EditText backgrounds to default
            resetEditTextBackgrounds();

            // Get the text from the EditText fields
            String name = nameEditText.getText().toString().trim();
            String surname = surnameEditText.getText().toString().trim();

            // Check if both name and surname are empty
            if (TextUtils.isEmpty(name) && TextUtils.isEmpty(surname)) {
                // Set red border for both nameEditText and surnameEditText
                nameEditText.setBackgroundResource(R.drawable.edittext_error);
                surnameEditText.setBackgroundResource(R.drawable.edittext_error);
                // Show toast message indicating that both fields are empty
                showToast("Please enter your name and surname");
                return; // Exit the method
            }

            // Check if name is empty
            if (TextUtils.isEmpty(name)) {
                // Set red border for nameEditText
                nameEditText.setBackgroundResource(R.drawable.edittext_error);
                // Show toast message indicating that name is empty
                showToast("Please enter your name");
                return; // Exit the method
            }

            // Check if surname is empty
            if (TextUtils.isEmpty(surname)) {
                // Set red border for surnameEditText
                surnameEditText.setBackgroundResource(R.drawable.edittext_error);
                // Show toast message indicating that surname is empty
                showToast("Please enter your surname");
                return; // Exit the method
            }

            // If both name and surname are not empty, proceed with the intent
            Intent intent = new Intent(ProfileSetupActivity.this, MainActivity.class);
            intent.putExtra("previousActivity", "ProfileSetupActivity");
            intent.putExtra("name", name);
            intent.putExtra("surname", surname);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        goToLoginTextView.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileSetupActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackPressTime > 2000) {
            backPressCounter = 0;
        }

        backPressCounter++;

        if (backPressCounter == 2) {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        } else if (backPressCounter >= 3) {
            finishAffinity();
            return;
        }

        lastBackPressTime = currentTime;
    }

    // Function to reset EditText backgrounds to default
    private void resetEditTextBackgrounds() {
        nameEditText.setBackgroundResource(R.drawable.custom_edittext);
        surnameEditText.setBackgroundResource(R.drawable.custom_edittext);
    }

    // Function to show Toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
