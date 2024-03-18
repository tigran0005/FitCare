package com.example.fitcarehub;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChoosePlanActivity extends AppCompatActivity {

    private int backPressCounter = 0;
    private long lastBackPressTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan);

        Button loseFatBtn = findViewById(R.id.loseFatButtonChoosePlanActivity);
        Button gainMuscleBtn = findViewById(R.id.musculeGainButtonChoosePlanActivity);

        loseFatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plan = "loseFat";
                Intent intent = new Intent(ChoosePlanActivity.this, ChoosePlaceActivity.class);
                intent.putExtra("plan", plan);
                startActivity(intent);
                finish();
            }
        });

        gainMuscleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String plan = "gainMuscle";
                Intent intent = new Intent(ChoosePlanActivity.this, ChoosePlaceActivity.class);
                intent.putExtra("plan", plan);
                startActivity(intent);
                finish();
            }
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
