package com.example.fitcarehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoosePlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_place);

        Button atHomeBtn = findViewById(R.id.atHomeButtonChoosePlaceActivity);
        Button atGymBtn = findViewById(R.id.atGymButtonChoosePlaceActivity);

        String plan = getIntent().getStringExtra("plan");

        atHomeBtn.setOnClickListener(v -> navigateToChoosePlanActivity("atHome", plan));

        atGymBtn.setOnClickListener(v -> navigateToChoosePlanActivity("atGym", plan));
    }

    private void navigateToChoosePlanActivity(String choosePlace, String plan) {
        Intent intent = new Intent(ChoosePlaceActivity.this, MainActivity.class);
        intent.putExtra("place", choosePlace);
        intent.putExtra("plan", plan);
        startActivity(intent);
        // finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
