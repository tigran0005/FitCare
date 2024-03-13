package com.example.fitcarehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.fitcarehub.databinding.ActivityProfileSetupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileSetupActivity extends AppCompatActivity {
    private int backPressCounter = 0;
    private long lastBackPressTime = 0;
    private ActivityProfileSetupBinding binding;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileSetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        binding.continueBtn.setOnClickListener(view -> {
            String name = binding.name.getText().toString().trim();
            String surname = binding.surname.getText().toString().trim();

            if (validateInputs(name, surname)) {
                saveUserToFirestore(name, surname);
            }
        });

        binding.goToLogin.setOnClickListener(view -> navigateToLogin());
    }

    private void saveUserToFirestore(String name, String surname) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String capitalizedFirstName = name.substring(0, 1).toUpperCase() + name.substring(1);
            String capitalizedLastName = surname.substring(0, 1).toUpperCase() + surname.substring(1);

            Map<String, Object> userData = new HashMap<>();
            userData.put("name", capitalizedFirstName);
            userData.put("surname", capitalizedLastName);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userId).set(userData)
                    .addOnSuccessListener(aVoid -> {
                        showToast("Профиль успешно сохранен");
                        navigateToMainActivity(capitalizedFirstName, capitalizedLastName);
                    })
                    .addOnFailureListener(e -> showToast("Не удалось сохранить профиль"));
        } else {
            showToast("Пользователь не вошел в систему");
        }
    }

    private void navigateToMainActivity(String name, String surname) {
        Intent intent = new Intent(ProfileSetupActivity.this, MainActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("surname", surname);
        startActivity(intent);
    }

    private boolean validateInputs(String name, String surname) {
        boolean isValid = true;
        if (TextUtils.isEmpty(name)) {
            binding.name.setBackgroundResource(R.drawable.edittext_error);
            showToast("Введите свое имя");
            isValid = false;
        }
        if (TextUtils.isEmpty(surname)) {
            binding.surname.setBackgroundResource(R.drawable.edittext_error);
            showToast("Введите свою фамилию");
            isValid = false;
        }
        return isValid;
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ProfileSetupActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackPressTime > 2000) {
            backPressCounter = 0;
        }

        backPressCounter++;

        if (backPressCounter == 1) {
            Toast.makeText(this, "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();
        } else if (backPressCounter >= 2) {
            finishAffinity();
            return;
        }

        lastBackPressTime = currentTime;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
