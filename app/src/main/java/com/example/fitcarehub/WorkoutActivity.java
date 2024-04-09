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

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class WorkoutActivity extends AppCompatActivity {

    private int currentWorkoutIndex = 0;
    private List<WorkoutItem> currentWorkoutItems;
    private TextView exerciseNameTextView, exerciseCountTextView;
    private ImageView backArrow, forwardArrow, backToPreWorkoutArrow;
    private GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        bindViews();
        loadDataFromIntent();
        setupWorkoutItems();
        setupViewListeners();
        updateUI();
    }

    private void bindViews() {
        exerciseNameTextView = findViewById(R.id.workoutActivityExerciseName);
        exerciseCountTextView = findViewById(R.id.exercisesCount);
        gifImageView = findViewById(R.id.workoutActivityGif);
        backArrow = findViewById(R.id.arrowBack);
        forwardArrow = findViewById(R.id.arrowNext);
        backToPreWorkoutArrow = findViewById(R.id.backToPreWorkoutArrow);
        findViewById(R.id.doneButton).setOnClickListener(v -> navigateToRestActivity(false));
    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();
        currentWorkoutIndex = intent != null ? intent.getIntExtra("CurrentWorkoutIndex", 0) : 0;
    }

    private void setupWorkoutItems() {
        Workout armsWorkout = new Workout("Arms");
        armsWorkout.addWorkoutItem(new WorkoutItem("https://i.pinimg.com/originals/8c/53/27/8c532774e4e1c524576bf1fb829ad895.gif", "Bicep Curls", 0, 15, 60));
        armsWorkout.addWorkoutItem(new WorkoutItem("https://i.pinimg.com/originals/8c/53/27/8c532774e4e1c524576bf1fb829ad895.gif", "Bicep Curls", 0, 12, 60));
        armsWorkout.addWorkoutItem(new WorkoutItem("https://i.pinimg.com/originals/8c/53/27/8c532774e4e1c524576bf1fb829ad895.gif", "Bicep Curls", 0, 10, 60));
        currentWorkoutItems = armsWorkout.getItems();
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
        if (currentWorkoutIndex < currentWorkoutItems.size() - 1 || isDoneClicked) {
            if (!isDoneClicked) {
                currentWorkoutIndex++;
            }
            WorkoutItem currentItem = currentWorkoutItems.get(currentWorkoutIndex);
            Intent intent = new Intent(this, RestActivity.class);
            intent.putExtra("NextWorkoutIndex", currentWorkoutIndex);
            intent.putExtra("RestTimeInSeconds", currentItem.getRestTime());
            startActivity(intent);
            finish();
        } else {
            increaseTheFinishedCount();
        }
    }

    private void increaseTheFinishedCount() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            DocumentReference userDocRef = db.collection("Users").document(user.getUid());
            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                Long finishedWorkouts = documentSnapshot.getLong("finishedWorkouts");
                if (finishedWorkouts == null) finishedWorkouts = 0L;
                userDocRef.update("finishedWorkouts", finishedWorkouts + 1)
                        .addOnSuccessListener(aVoid -> {
                            Intent intent = new Intent(this, WorkoutsFragment.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> Toast.makeText(WorkoutActivity.this, "Failed to update workout count", Toast.LENGTH_SHORT).show());
            });
        }
    }


    private void navigateBackToPreWorkout() {
        startActivity(new Intent(this, PreWorkoutActivity.class));
        finish();
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
