package com.example.fitcarehub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<com.example.fitcarehub.User> dataList;
    private Context context;

    public LeaderboardAdapter(Context context, List<com.example.fitcarehub.User> dataList) { // Fully qualified name
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.leaderboard_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        com.example.fitcarehub.User user = dataList.get(position);
        holder.textViewTitle.setText(user.getName() + " " + user.getSurname());
        holder.score.setText(String.valueOf(user.getScore()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageView;
        TextView textViewTitle, score;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.leaderboard_image);
            textViewTitle = itemView.findViewById(R.id.leaderboard_text);
            score = itemView.findViewById(R.id.leaderboard_score);
        }
    }
}
