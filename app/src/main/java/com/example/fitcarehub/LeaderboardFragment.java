package com.example.fitcarehub;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private List<User> dataList;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewLeaderboard);
        dataList = new ArrayList<>();
        adapter = new LeaderboardAdapter(getContext(), dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        getUsersAndUpdateScores();

        return view;
    }



    private void getUsersAndUpdateScores() {
        db.collection("Users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String name = documentSnapshot.getString("name");
                            String surname = documentSnapshot.getString("surname");
                            long calories = documentSnapshot.getLong("caloriesBurnt");
                            long finishedWorkouts = documentSnapshot.getLong("finishedWorkouts");
                            long totalTimeSpent = documentSnapshot.getLong("totalTimeSpent");
                            long score = (totalTimeSpent / 100 * calories * finishedWorkouts) * 1 / 20;
                            User user = new User(name, surname, (int) score);
                            dataList.add(user);
                        }
                        Collections.sort(dataList, new Comparator<User>() {
                            @Override
                            public int compare(User user1, User user2) {
                                return Long.compare(user2.getScore(), user1.getScore());
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }
}
