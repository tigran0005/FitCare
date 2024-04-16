package com.example.fitcarehub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class WorkoutActivity extends AppCompatActivity {

    private int currentWorkoutIndex = 0;
    private List<WorkoutItem> currentWorkoutItems = new ArrayList<>();
    private TextView exerciseNameTextView, exerciseCountTextView;
    private ImageView backArrow, forwardArrow, backToPreWorkoutArrow;
    private GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        bindViews();
        setupViewListeners();

        fetchWorkoutsFromFirestore();

        if (getIntent().hasExtra("CurrentWorkoutIndex")) {
            currentWorkoutIndex = getIntent().getIntExtra("CurrentWorkoutIndex", 0);
        }
    }

    private void bindViews() {
        exerciseNameTextView = findViewById(R.id.workoutActivityExerciseName);
        exerciseCountTextView = findViewById(R.id.exercisesCount);
        gifImageView = findViewById(R.id.workoutActivityGif);
        backArrow = findViewById(R.id.arrowBack);
        forwardArrow = findViewById(R.id.arrowNext);
        backToPreWorkoutArrow = findViewById(R.id.backToPreWorkoutArrow);

        findViewById(R.id.doneButton).setOnClickListener(v -> navigateToRestActivity(true));
    }

    private void fetchWorkoutsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Workouts").document("UtuipVntK8BQY85OoB1F").collection("Arms1")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        currentWorkoutItems.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String gif = document.getString("gif");
                            String name = document.getString("workoutName");
                            int count = document.getLong("Count").intValue();
                            int restTime = document.getLong("restTime").intValue();

                            currentWorkoutItems.add(new WorkoutItem(gif, name, 0, count, restTime));
                        }

                        if (!currentWorkoutItems.isEmpty()) {
                            updateUI();
                        } else {
                            Toast.makeText(WorkoutActivity.this, "No workouts found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WorkoutActivity.this, "Failed to load workouts.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupViewListeners() {
        backArrow.setOnClickListener(v -> navigateToPreviousWorkout());
        forwardArrow.setOnClickListener(v -> navigateToRestActivity(false));
        backToPreWorkoutArrow.setOnClickListener(v -> navigateBackToPreWorkout());
    }

    private void navigateToPreviousWorkout() {
        if (currentWorkoutIndex > 0) {
            currentWorkoutIndex--;
            updateUI();
        }
    }


    private void navigateToRestActivity(boolean isDoneClicked) {
        if (currentWorkoutIndex < currentWorkoutItems.size() - 1) {
            currentWorkoutIndex++;
            WorkoutItem currentItem = currentWorkoutItems.get(currentWorkoutIndex);
            Intent intent = new Intent(this, RestActivity.class);
            intent.putExtra("NextWorkoutIndex", currentWorkoutIndex);
            intent.putExtra("RestTimeInSeconds", currentItem.getRestTime());
            intent.putExtra("TotalWorkouts", currentWorkoutItems.size());
            intent.putExtra("ExerciseName", currentItem.getName());
            intent.putExtra("ExerciseCount", currentItem.getCount());
            intent.putExtra("ExerciseGif", currentItem.getGif());
            startActivity(intent);
            finish();
        } else if (isDoneClicked || currentWorkoutIndex == currentWorkoutItems.size() - 1) {
            increaseTheFinishedCount();
            Intent intent = new Intent(WorkoutActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }



    }

    private void increaseTheFinishedCount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userDocRef = db.collection("Users").document(user.getUid());
            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                Long finishedWorkouts = documentSnapshot.getLong("finishedWorkouts");
                if (finishedWorkouts == null) {
                    finishedWorkouts = 0L;
                }
                finishedWorkouts++;
                userDocRef.update("finishedWorkouts", finishedWorkouts)
                        .addOnSuccessListener(aVoid -> Toast.makeText(WorkoutActivity.this, "Workout count updated successfully!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(WorkoutActivity.this, "Failed to update workout count", Toast.LENGTH_SHORT).show());
            }).addOnFailureListener(e -> Toast.makeText(WorkoutActivity.this, "Error fetching workout count", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateBackToPreWorkout() {
        startActivity(new Intent(this, PreWorkoutActivity.class));
        finish();
    }

    private void updateUI() {
        if (currentWorkoutIndex >= 0 && currentWorkoutIndex < currentWorkoutItems.size()) {
            WorkoutItem currentItem = currentWorkoutItems.get(currentWorkoutIndex);
            exerciseNameTextView.setText(currentItem.getName());
            exerciseCountTextView.setText("x " + currentItem.getCount());

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
