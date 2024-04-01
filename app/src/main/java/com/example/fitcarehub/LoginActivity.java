package com.example.fitcarehub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private TextView signUpRedirectText, forgotPassword;
    private FirebaseAuth auth;
    private boolean isForgotPasswordClicked = false;
    private int backPressCounter = 0;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signUpRedirectText = findViewById(R.id.signUpRedirectText);
        forgotPassword = findViewById(R.id.forgot_password);

        auth = FirebaseAuth.getInstance();


        String place = getIntent().getStringExtra("place");
        String plan = getIntent().getStringExtra("plan");
        String savedEmail = getEmailFromSharedPreferences();
        loginEmail.setText(savedEmail);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
                loginUser();
            }
        });

        signUpRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isForgotPasswordClicked) {
                    isForgotPasswordClicked = true;
                    showForgotPasswordDialog();
                }
            }
        });
    }

    private void loginUser() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (!validateForm(email, password)) {
            loginButton.setEnabled(true);
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null && user.isEmailVerified()) {
                        SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                        editor.putBoolean("isLogged", true);
                        editor.apply();
                        navigateToMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please verify your email.", Toast.LENGTH_SHORT).show();
                        loginButton.setEnabled(true);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(LoginActivity.this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    loginButton.setEnabled(true);
                });

    }

    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
        EditText emailBox = dialogView.findViewById(R.id.emailBox);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = emailBox.getText().toString().trim();

                if (TextUtils.isEmpty(userEmail) || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(LoginActivity.this, "Введите действительный адрес электронной почты", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.sendPasswordResetEmail(userEmail)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Проверьте свою электронную почту для получения инструкций по сбросу пароля", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Не удалось отправить письмо для сброса пароля", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                            isForgotPasswordClicked = false;
                        });
            }
        });

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                isForgotPasswordClicked = false;
            }
        });

        dialog.show();
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

    private String getEmailFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("email", "");
    }


    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Введите действительный адрес электронной почты");
            valid = false;
        } else {
            loginEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            loginPassword.setError("Введите ваш пароль");
            valid = false;
        } else {
            loginPassword.setError(null);
        }

        return valid;
    }

    private void sendVerificationEmail() {
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Письмо с подтверждением отправлено на " + auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Не удалось отправить письмо с подтверждением.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void checkUserNameAndSurname() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("Users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {
                            String name = document.getString("name");
                            String surname = document.getString("surname");

                            Intent intent;
                            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(surname)) {
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                            } else {
                                intent = new Intent(LoginActivity.this, ProfileSetupActivity.class);
                            }

                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Что то пошло не так.", Toast.LENGTH_SHORT).show();
                        loginButton.setEnabled(true);
                    }
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "Что то пошло не так.", Toast.LENGTH_SHORT).show();
            loginButton.setEnabled(true);
        }
    }


    private void isEmailVerified() {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null && user.isEmailVerified()) {
            checkUserNameAndSurname();
        } else {
            Toast.makeText(LoginActivity.this, "Пожалуйста, подтвердите вашу электронную почту.", Toast.LENGTH_SHORT).show();
            loginButton.setEnabled(true);
        }
    }

    private void navigateToMainActivity() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(user.getUid()).get().addOnCompleteListener(task -> {
                Intent intent;
                if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    String name = document.getString("name");
                    String surname = document.getString("surname");
                    if (name != null && surname != null) {
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("surname", surname);
                    } else {
                        intent = new Intent(LoginActivity.this, ProfileSetupActivity.class);
                    }
                } else {
                    intent = new Intent(LoginActivity.this, ProfileSetupActivity.class);
                }
                startActivity(intent);
                finish();
            });
        }
    }


}
