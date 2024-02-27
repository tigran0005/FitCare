package com.example.fitcarehub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int backPressCounter = 0;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        TextView welcomeTextView = findViewById(R.id.Welcome);
        welcomeTextView.setText("Hi, " + name);

        Button buttonHome = findViewById(R.id.button_home);
        Button buttonProfile = findViewById(R.id.button_profile);
        Button buttonSettings = findViewById(R.id.button_settings);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Home button clicked");
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Settings button clicked");
            }
        });
    }

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

        if (backPressCounter == 2) {
            Toast.makeText(this, "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
        } else if (backPressCounter >= 3) {
            finishAffinity();
            return;
        }

        lastBackPressTime = currentTime;
    }
}
