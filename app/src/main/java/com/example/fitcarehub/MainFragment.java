package com.example.fitcarehub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainFragment extends Fragment {
    ImageView profilePicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setImageToProfilePicture();


        View view = inflater.inflate(R.layout.fragment_main, container, false);
        profilePicture = view.findViewById(R.id.profileImageMainFragment);
        TextView welcomeTextView = view.findViewById(R.id.Welcome);
        ConstraintLayout constraintLayout = view.findViewById(R.id.mainFragmentArms);



        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PreWorkoutActivity.class);
                startActivity(intent);
            }
        });


        Bundle args = getArguments();
        String welcomeText;
        if (args != null) {
            boolean isGuest = args.getBoolean("isGuest", false);

            if (isGuest) {
                welcomeText = "Привет, Гость";
            } else {
                String name = args.getString("name", "");
                String surname = args.getString("surname", "");
                if (!name.isEmpty() || !surname.isEmpty()) {
                    welcomeText = "Привет, " + name;
                } else {
                    welcomeText = "Добро пожаловать";
                }
            }
        } else {
            welcomeText = "Добро пожаловать";
        }

        welcomeTextView.setText(welcomeText);

        return view;
    }

    private void setImageToProfilePicture() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    DocumentSnapshot document = task.getResult();
                    String imageUrl = document.getString("profileImageUrl");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Glide.with(this)
                                .load(imageUrl)
                                .into(profilePicture);
                    }
                }
            });
        }
    }
}
