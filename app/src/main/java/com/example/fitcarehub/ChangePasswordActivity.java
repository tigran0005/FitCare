package com.example.fitcarehub;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.view.Change;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText changePasswordOldPassword;
    private EditText changePasswordNewPassword;
    private EditText changePasswordConfirmNewPassword;
    private Button saveButton;
    private ImageView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        changePasswordOldPassword = findViewById(R.id.changePasswordOldPassword);
        changePasswordNewPassword = findViewById(R.id.changePasswordNewPassowrd);
        changePasswordConfirmNewPassword = findViewById(R.id.changePasswordConfrimNewPassword);
        saveButton = findViewById(R.id.saveButtonchangePassword);
        arrow = findViewById(R.id.backToEditAccountArrow);



        arrow.setOnClickListener(v -> {
            Intent intent = new Intent(ChangePasswordActivity.this, EditAccountActivity.class);
            startActivity(intent);
            finish();
        });

        saveButton.setOnClickListener(v -> {
            savePassword();
        });

    }



    private void savePassword() {
        String oldPassword = changePasswordOldPassword.getText().toString().trim();
        String newPassword = changePasswordNewPassword.getText().toString().trim();
        String confirmPassword = changePasswordConfirmNewPassword.getText().toString().trim();

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showToast("Please fill in all the fields.");
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            showToast("Password updated successfully.");
                                            navigateToMainActivity();
                                        } else {
                                            showToast("Password update failed. Please try again later.");
                                        }
                                    });
                        } else {
                            showToast("Old password is incorrect. Please try again.");
                        }
                    });
        } else {
            showToast("User is not logged in. Please log in and try again.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backToEditAccountArrow) {
            Intent intent = new Intent(ChangePasswordActivity.this, EditAccountActivity.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.saveButtonchangePassword) {
            savePassword();
        }
    }


}
