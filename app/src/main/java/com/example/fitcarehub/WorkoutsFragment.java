package com.example.fitcarehub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WorkoutsFragment extends Fragment {
    private ProgressBar progressBar;
    private TextView progressText;
    private ConstraintLayout armsWorkout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workouts, container, false);

        progressBar = rootView.findViewById(R.id.progressBar);
        progressText = rootView.findViewById(R.id.progressTextView);
        RadioGroup radioGroup = rootView.findViewById(R.id.radioGroup);
        RadioButton radioButtonGainMuscle = rootView.findViewById(R.id.radioButtonGainMuscle);
        RadioButton radioButtonLoseFat = rootView.findViewById(R.id.radioButtonLoseFat);
        RadioButton radioButtonStayActive = rootView.findViewById(R.id.radioButtonStayActive);
        armsWorkout = rootView.findViewById(R.id.armsConstraintLayout);


        fetchProgressFromFirestore();
        controlWorkoutsVisibility(radioGroup);

        ConstraintLayout armsConstraintLayout = rootView.findViewById(R.id.armsConstraintLayout);
        armsConstraintLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PreWorkoutActivity.class);
            startActivity(intent);
        });

        return rootView;
    }

    private void fetchProgressFromFirestore() { // doesnt work
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Users").document(userId).collection("WorkoutInfo").document("WorkoutNumber")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            Long workoutNumber = document.getLong("WorkoutNumber");

                            if (workoutNumber != null) {
                                db.collection("Users").document(userId).collection("WorkoutInfo").document("Size")
                                        .get()
                                        .addOnCompleteListener(sizeTask -> {
                                            if (sizeTask.isSuccessful() && sizeTask.getResult() != null) {
                                                DocumentSnapshot sizeDocument = sizeTask.getResult();
                                                Long totalSize = sizeDocument.getLong("Size");

                                                if (totalSize != null) {
                                                    String progressTextString = String.format("%d/%d", workoutNumber, totalSize.intValue());
                                                    requireActivity().runOnUiThread(() -> progressText.setText(progressTextString));
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }
    }







    private void controlWorkoutsVisibility(RadioGroup radioGroup) {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonGainMuscle) {
                armsWorkout.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radioButtonLoseFat || checkedId == R.id.radioButtonStayActive) {
                armsWorkout.setVisibility(View.GONE);
            } else {
                armsWorkout.setVisibility(View.VISIBLE);
            }
        });
    }


    public void updateProgressBar(int progress) {
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
    }
}
