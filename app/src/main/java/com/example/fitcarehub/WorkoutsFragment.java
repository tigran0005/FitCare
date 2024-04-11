package com.example.fitcarehub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

public class WorkoutsFragment extends Fragment {
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workouts, container, false);

        progressBar = rootView.findViewById(R.id.progressBar);

        ConstraintLayout armsConstraintLayout = rootView.findViewById(R.id.armsConstraintLayout);
        armsConstraintLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PreWorkoutActivity.class);
            startActivity(intent);
        });

        updateProgressBar(20);  

        return rootView;
    }

    public void updateProgressBar(int progress) {
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
    }
}
