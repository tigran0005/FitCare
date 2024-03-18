package com.example.fitcarehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class ChoosePlaceActivity extends AppCompatActivity {
    private int backPressCounter = 0;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_place);
        Button atHomeBtn = findViewById(R.id.atHomeButtonChoosePlaceActivity);
        Button atGymBtn = findViewById(R.id.atGymButtonChoosePlaceActivity);

        atHomeBtn.setOnClickListener(v -> {
            String choosePlace = "atHome";
            Intent intent = new Intent(ChoosePlaceActivity.this, MainActivity.class);
            intent.putExtra("place", choosePlace);
            startActivity(intent);
            finish();
        });

        atGymBtn.setOnClickListener(v -> {
            String choosePlace = "atGym";
            Intent intent = new Intent(ChoosePlaceActivity.this, MainActivity.class);
            intent.putExtra("place", choosePlace);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackPressTime > 2000) {
            backPressCounter = 0;
        }

        backPressCounter++;

        if (backPressCounter == 1) {
            Toast.makeText(this, "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
        } else if (backPressCounter >= 2) {
            finishAffinity();
            return;
        }

        lastBackPressTime = currentTime;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
