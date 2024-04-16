package com.example.fitcarehub;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import pl.droidsonroids.gif.GifImageView;

public class RestActivity extends AppCompatActivity {

    private Button skipBtn, addTimeBtn;
    private CountDownTimer timer;
    private long remainingTimeMillis;
    private TextView timerTextView, nextWorkoutTextView, exerciseNameTextView, exerciseCountTextView;
    private GifImageView gif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        skipBtn = findViewById(R.id.skipExercise);
        addTimeBtn = findViewById(R.id.addSeconds);
        timerTextView = findViewById(R.id.timerTextView);
        nextWorkoutTextView = findViewById(R.id.nextWorkoutTextView);
        exerciseNameTextView = findViewById(R.id.exerciseNameTextView);
        exerciseCountTextView = findViewById(R.id.exerciseCountTextView);
        gif = findViewById(R.id.workoutActivityGif);

        long defaultRestTime = 10;
        long restTime = getIntent().getLongExtra("RestTimeInSeconds", defaultRestTime);
        int nextWorkoutIndex = getIntent().getIntExtra("NextWorkoutIndex", 0);
        int totalWorkouts = getIntent().getIntExtra("TotalWorkouts", 0);
        int exerciseCount = getIntent().getIntExtra("ExerciseCount", 0);
        String exerciseGifUrl = getIntent().getStringExtra("ExerciseGif");
        String exerciseName = getIntent().getStringExtra("ExerciseName");

        if (exerciseNameTextView != null) {
            exerciseNameTextView.setText(exerciseName);
        }
        if (nextWorkoutTextView != null) {
            nextWorkoutTextView.setText(String.format("Next %d/%d", nextWorkoutIndex + 1, totalWorkouts));
        }
        if (exerciseCountTextView != null) {
            exerciseCountTextView.setText(String.format("x %d", exerciseCount));
        }
        if (gif != null) {
            RequestOptions options = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop();

            Glide.with(this)
                    .asGif()
                    .load(exerciseGifUrl)
                    .apply(options)
                    .into(gif);
        }

        startTimer(restTime);
        setupButtons();
    }

    private void startTimer(long durationInSeconds) {
        timer = new CountDownTimer(durationInSeconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                remainingTimeMillis = millisUntilFinished;
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
            }

            public void onFinish() {
                timerTextView.setText("00:00");
                navigateToWorkoutActivity();
            }
        }.start();
    }

    private void addExtraTime(long extraSeconds) {
        timer.cancel();
        startTimer((remainingTimeMillis + (extraSeconds * 1000)) / 1000);
    }

    private void skipExercise() {
        timer.cancel();
        navigateToWorkoutActivity();
    }

    private void navigateToWorkoutActivity() {
        Intent intent = new Intent(RestActivity.this, WorkoutActivity.class);
        int nextWorkoutIndex = getIntent().getIntExtra("NextWorkoutIndex", 0);
        intent.putExtra("CurrentWorkoutIndex", nextWorkoutIndex);
        startActivity(intent);
        finish();
    }

    private void setupButtons() {
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipExercise();
            }
        });

        addTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExtraTime(10);
            }
        });
    }
}
