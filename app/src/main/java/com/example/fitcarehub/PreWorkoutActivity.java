package com.example.fitcarehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class PreWorkoutActivity extends AppCompatActivity {



    Button button;
    ImageView arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_workout);

        RecyclerView recyclerView = findViewById(R.id.exercisesRecyclerView);


        List<PreWorkout> items = new ArrayList<PreWorkout>();
        items.add(new PreWorkout("Arm Curls", 10, R.drawable.cardiowomangif));
        items.add(new PreWorkout("A", 10, R.drawable.cardiowomangif));
        items.add(new PreWorkout("B", 10, R.drawable.cardiowomangif));
        items.add(new PreWorkout("C", 10, R.drawable.cardiowomangif));
        items.add(new PreWorkout("D", 10, R.drawable.cardiowomangif));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), items));


        arrow = findViewById(R.id.arrowPreWorkout);
        button = findViewById(R.id.myStickyButton);


        arrow.setOnClickListener(v -> {
//            Intent intent = new Intent(PreWorkoutActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
            onBackPressed();
        });

        button.setOnClickListener(v -> {
            Intent intent = new Intent(PreWorkoutActivity.this, WorkoutActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}