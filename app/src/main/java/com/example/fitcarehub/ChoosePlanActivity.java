package com.example.fitcarehub;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoosePlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan);

        Button loseFatBtn = findViewById(R.id.loseFatButtonChoosePlanActivity);
        Button gainMuscleBtn = findViewById(R.id.musculeGainButtonChoosePlanActivity);

        loseFatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToChoosePlaceActivity("loseFat");
            }
        });

        gainMuscleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToChoosePlaceActivity("gainMuscle");
            }
        });
    }

    private void navigateToChoosePlaceActivity(String plan) {
        Intent intent = new Intent(ChoosePlanActivity.this, ChoosePlaceActivity.class);
        intent.putExtra("plan", plan);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
