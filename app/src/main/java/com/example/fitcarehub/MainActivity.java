package com.example.fitcarehub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    private int backPressCounter = 0;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<<<< Temporary merge branch 1
        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        TextView welcomeTextView = findViewById(R.id.Welcome);
        welcomeTextView.setText("Hi, " + name);

=========
        // Initialize your buttons
>>>>>>>>> Temporary merge branch 2
        Button buttonHome = findViewById(R.id.button_home);
        Button buttonProfile = findViewById(R.id.button_profile);
        Button buttonSettings = findViewById(R.id.button_settings);

        // Set up button listeners
        buttonHome.setOnClickListener(v -> {
            // Replace the fragment with HomeFragment (or MainFragment)
            setFragment(new MainFragment());
        });

<<<<<<<<< Temporary merge branch 1
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
=========
        buttonProfile.setOnClickListener(v -> {
            // Replace the fragment with ProfileFragment
            setFragment(new ProfileFragment());
>>>>>>>>> Temporary merge branch 2
        });

        buttonSettings.setOnClickListener(v -> {
            showToast("Settings button clicked");
        });
    }

    // Helper method for showing toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackPressTime > 2000) {
            backPressCounter = 0;
        }

        backPressCounter++;

<<<<<<<<< Temporary merge branch 1
        if (backPressCounter == 2) {
            Toast.makeText(this, "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
        } else if (backPressCounter >= 3) {
            finishAffinity();
            return;
        }

        lastBackPressTime = currentTime;
=========
        if (backPressCounter == 1) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        } else if (backPressCounter >= 2) {
            finishAffinity();
        }

        lastBackPressTime = currentTime;
    }

    // Method for setting the fragment without adding the transaction to the back stack
    private void setFragment(Fragment fragment) {
        // Ensure the container is cleared of all fragments. This might not be necessary
        // for a simple replace operation, but included here for clarity.
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Perform the replace transaction
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_id, fragment)
                // Optionally, you can add the transaction to the back stack if you want to navigate back.
                // .addToBackStack(null)
                .commitAllowingStateLoss(); // Using commitAllowingStateLoss() to handle edge cases where state might be lost.
>>>>>>>>> Temporary merge branch 2
    }
}