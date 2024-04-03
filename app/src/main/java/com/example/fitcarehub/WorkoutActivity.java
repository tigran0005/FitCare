package com.example.fitcarehub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import pl.droidsonroids.gif.GifImageView;

public class WorkoutActivity extends AppCompatActivity {
    int currentWorkoutIndex = 0;
    List<WorkoutItem> currentWorkoutItems;
    private TextView exerciseNameTextView, exerciseCountTextView;
    private ImageView backArrow, forwardArrow, backToPreWorkoutArrow;
    private GifImageView gifImageView;
    private Button doneButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        exerciseNameTextView = findViewById(R.id.workoutActivityExerciseName);
        backArrow = findViewById(R.id.arrowBack);
        forwardArrow = findViewById(R.id.arrowNext);
        backToPreWorkoutArrow = findViewById(R.id.backToPreWorkoutArrow);
        exerciseCountTextView = findViewById(R.id.exercisesCount);
        gifImageView = findViewById(R.id.workoutActivityGif);
        doneButton = findViewById(R.id.doneButton);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CurrentWorkoutIndex")) {
            currentWorkoutIndex = intent.getIntExtra("CurrentWorkoutIndex", 0);
        }

        Workout armsWorkout = new Workout("Arms");
        armsWorkout.addWorkoutItem(new WorkoutItem("https://images.ctfassets.net/8urtyqugdt2l/4hhf4xvRCpIeqU0UsdqgcQ/840ab4343d9e820c691b281fdd9ce759/_uploads_1571836981-barbell-bicep-curl.gif", "Bicep Curls", 0, 15, 60));
        armsWorkout.addWorkoutItem(new WorkoutItem("https://images.ctfassets.net/8urtyqugdt2l/4hhf4xvRCpIeqU0UsdqgcQ/840ab4343d9e820c691b281fdd9ce759/_uploads_1571836981-barbell-bicep-curl.gif", "Bicep Curls", 0, 12, 60));
        armsWorkout.addWorkoutItem(new WorkoutItem("https://images.ctfassets.net/8urtyqugdt2l/4hhf4xvRCpIeqU0UsdqgcQ/840ab4343d9e820c691b281fdd9ce759/_uploads_1571836981-barbell-bicep-curl.gif", "Bicep Curls", 0, 10, 60));
        currentWorkoutItems = armsWorkout.getItems();

        if (!currentWorkoutItems.isEmpty()) {
            updateUI();
        }

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentWorkoutIndex > 0) {
                    currentWorkoutIndex--;
                    updateUI();
                }
            }


        });

        forwardArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRestActivity();
            }

            private void navigateToRestActivity() {
                if (currentWorkoutIndex >= 0 && currentWorkoutIndex < currentWorkoutItems.size()) {
                    WorkoutItem currentItem = currentWorkoutItems.get(currentWorkoutIndex);
                    int restTime = currentItem.getRestTime();

                    Intent intent = new Intent(WorkoutActivity.this, RestActivity.class);
                    int nextWorkoutIndex = currentWorkoutIndex < currentWorkoutItems.size() - 1 ? currentWorkoutIndex + 1 : currentWorkoutIndex;
                    intent.putExtra("NextWorkoutIndex", nextWorkoutIndex);
                    intent.putExtra("RestTimeInSeconds", restTime);
                    startActivity(intent);
                    finish();
                }
            }

        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToRestActivity();
            }

            private void navigateToRestActivity() {
                Intent intent = new Intent(WorkoutActivity.this, RestActivity.class);
                int nextWorkoutIndex = currentWorkoutIndex < currentWorkoutItems.size() - 1 ? currentWorkoutIndex + 1 : currentWorkoutIndex;
                intent.putExtra("NextWorkoutIndex", nextWorkoutIndex);
                startActivity(intent);
                finish();
            }
        });

        backToPreWorkoutArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkoutActivity.this, PreWorkoutActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }

    private void updateUI() {
        if (currentWorkoutIndex >= 0 && currentWorkoutIndex < currentWorkoutItems.size()) {
            WorkoutItem currentItem = currentWorkoutItems.get(currentWorkoutIndex);
            if (exerciseNameTextView != null) {
                exerciseNameTextView.setText(currentItem.getName());
            }
            if (exerciseCountTextView != null) {
                exerciseCountTextView.setText("x " + currentItem.getCount());
            }
            if (gifImageView != null) {
                RequestOptions options = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop();

                Glide.with(this)
                        .asGif()
                        .load(currentItem.getGif())
                        .apply(options)
                        .into(gifImageView);
            }
        }
    }
}
