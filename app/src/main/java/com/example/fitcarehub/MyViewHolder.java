package com.example.fitcarehub;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pl.droidsonroids.gif.GifImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView name, duration;
    GifImageView gif;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.itemExerciseName);
        duration = itemView.findViewById(R.id.itemExerciseDuration);
        gif = itemView.findViewById(R.id.itemExerciseGif);
    }
}
