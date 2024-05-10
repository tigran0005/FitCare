package com.example.fitcarehub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PreWorkoutActivity extends AppCompatActivity {

    private List<PreWorkout> items;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_workout);

        RecyclerView recyclerView = findViewById(R.id.exercisesRecyclerView);
        TextView workoutTypeText = findViewById(R.id.workoutTypePreWorkout);
        ImageView workoutImage = findViewById(R.id.workoutImagePreWorkout);

        items = new ArrayList<>();
        String workoutType = getIntent().getStringExtra("workoutType");
        if ("arms".equals(workoutType)) {
            workoutTypeText.setText("Arms");
            workoutImage.setImageResource(R.drawable.paintedmanarms);
            items.add(new PreWorkout("Arm Curls", 10, R.drawable.cardiowomangif));
            items.add(new PreWorkout("Arm Exercise A", 10, R.drawable.cardiowomangif));
            items.add(new PreWorkout("Arm Exercise B", 10, R.drawable.cardiowomangif));
        } else if ("legs".equals(workoutType)) {
            workoutTypeText.setText("Legs");
            workoutImage.setImageResource(R.drawable.paintedmanleg);
            items.add(new PreWorkout("Leg Press", 10, R.drawable.cardiowomangif));
            items.add(new PreWorkout("Leg Exercise A", 10, R.drawable.cardiowomangif));
            items.add(new PreWorkout("Leg Exercise B", 10, R.drawable.cardiowomangif));
        } else if ("cardio".equals(workoutType)) {
            workoutTypeText.setText("Cardio");
            workoutImage.setImageResource(R.drawable.paintedgirlrunningwithweights2);
            items.add(new PreWorkout("Running", 10, R.drawable.cardiowomangif));
            items.add(new PreWorkout("Jumping Jacks", 10, R.drawable.cardiowomangif));
            items.add(new PreWorkout("High Knees", 10, R.drawable.cardiowomangif));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(getApplicationContext(), items);
        recyclerView.setAdapter(adapter);

        ImageView arrow = findViewById(R.id.arrowPreWorkout);
        Button button = findViewById(R.id.myStickyButton);

        arrow.setOnClickListener(v -> onBackPressed());

        button.setOnClickListener(v -> {
            Intent intent = new Intent(PreWorkoutActivity.this, WorkoutActivity.class);
            intent.putExtra("workoutType", workoutType);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
