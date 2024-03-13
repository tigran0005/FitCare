package com.example.fitcarehub;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private int backPressCounter = 0;
    private long lastBackPressTime = 0;
    private Button activeButton = null;
    private Button[] buttons = new Button[3];
    private int[] buttonNormalIcons = new int[]{
            R.drawable.__icon__home_,
            R.drawable.__icon__local_fire_department_,
            R.drawable.__icon__person_
    };
    private int[] buttonActiveIcons = new int[]{
            R.drawable.__icon__home_purpur,
            R.drawable.__icon__local_fire_department_purpur,
            R.drawable.__icon__person_purpur
    };
    private int[] buttonColors = new int[]{R.color.normal_text_color, R.color.purpur};

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firestore = FirebaseFirestore.getInstance();

        buttons[0] = findViewById(R.id.button_home);
        buttons[1] = findViewById(R.id.button_profile);
        buttons[2] = findViewById(R.id.button_settings);

        initButtons();

        loadUserProfile();
    }

    private void initButtons() {
        setLargerIconAndTextColor(buttons[0], buttonActiveIcons[0], buttonColors[1]);
        activeButton = buttons[0];
        for (int i = 1; i < buttons.length; i++) {
            setLargerIconAndTextColor(buttons[i], buttonNormalIcons[i], buttonColors[0]);
        }
    }

    private void loadUserProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    DocumentSnapshot document = task.getResult();
                    String name = document.getString("name");
                    String surname = document.getString("surname");
                    String email = document.getString("email");
                    setupUIBasedOnUserProfile(name, surname);
                } else {
                    navigateToProfileSetupActivity();
                }
            });
        } else {
            navigateToLoginActivity();
        }
    }

    private void setupUIBasedOnUserProfile(String name, String surname) {
        boolean isGuest = getIntent().getBooleanExtra("isGuest", false);
        Bundle bundle = new Bundle();
        if (!isGuest && name != null && surname != null) {
            bundle.putString("name", name);
            bundle.putString("surname", surname);
        } else {
            bundle.putBoolean("isGuest", true);
        }

        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        switchFragment(mainFragment);

        setupButtonListeners(bundle);
    }

    private void setupButtonListeners(Bundle userBundle) {
        buttons[0].setOnClickListener(v -> {
            MainFragment mainFragment = new MainFragment();
            mainFragment.setArguments(userBundle);
            switchFragment(mainFragment);
            setActiveButton(0);
        });

        buttons[1].setOnClickListener(v -> {
            WorkoutsFragment workoutsFragment = new WorkoutsFragment();
            switchFragment(workoutsFragment);
            setActiveButton(1);
        });

        buttons[2].setOnClickListener(v -> {
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setArguments(userBundle);
            switchFragment(profileFragment);
            setActiveButton(2);
        });
    }

    private void setLargerIconAndTextColor(Button button, int drawableId, int textColorId) {
        Drawable drawable = ContextCompat.getDrawable(this, drawableId);
        button.setTextColor(ContextCompat.getColor(this, textColorId));
        if (drawable != null) {
            drawable.setBounds(0, 0, 100, 100);
            button.setCompoundDrawables(null, drawable, null, null);
        }
    }

    private void setActiveButton(int activeIndex) {
        for (int i = 0; i < buttons.length; i++) {
            if (i == activeIndex) {
                setLargerIconAndTextColor(buttons[i], buttonActiveIcons[i], buttonColors[1]);
                activeButton = buttons[i];
            } else {
                setLargerIconAndTextColor(buttons[i], buttonNormalIcons[i], buttonColors[0]);
            }
        }
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_id, fragment)
                .setReorderingAllowed(true)
                .commit();
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToProfileSetupActivity() {
        Intent intent = new Intent(MainActivity.this, ProfileSetupActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackPressTime > 2000) { // 2 seconds
            backPressCounter = 0;
        }

        backPressCounter++;

        if (backPressCounter == 1) {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        } else if (backPressCounter >= 2) {
            super.onBackPressed();
            finishAffinity();
        }
        lastBackPressTime = currentTime;
    }
}
