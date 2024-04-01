package com.example.fitcarehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class PreWorkoutActivity extends AppCompatActivity {

    ImageView arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_workout);

        arrow = findViewById(R.id.arrowPreWorkout);


        arrow.setOnClickListener(v -> {
//            Intent intent = new Intent(PreWorkoutActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
            onBackPressed();
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}