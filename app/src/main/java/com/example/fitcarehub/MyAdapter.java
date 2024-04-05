package com.example.fitcarehub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<PreWorkout> items;

    public MyAdapter(Context context, List<PreWorkout> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PreWorkout item = items.get(position);
        holder.name.setText(item.getExerciseName());
        holder.duration.setText(String.valueOf(item.getTimeFormatted()) + " sec");
        holder.gif.setImageResource(item.getGif());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
