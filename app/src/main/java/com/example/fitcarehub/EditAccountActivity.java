package com.example.fitcarehub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditAccountActivity extends AppCompatActivity {

    TextView changePasswordText, editPhotoText;
    ImageView imageView, changePasswordArrow, backToProfileArrow;
    EditText nameField, surnameField;
    Button saveBtn;
    private static final int PICK_IMAGE = 1;
    Uri selectedImageUri;
    private int backPressCounter = 0;
    private long lastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        setImageToProfilePicture();

        imageView = findViewById(R.id.profileImageEditAccountActivity);
        nameField = findViewById(R.id.editAccountName);
        surnameField = findViewById(R.id.editAccountSurname);
        changePasswordText = findViewById(R.id.changePasswordTextEditProfile);
        changePasswordArrow = findViewById(R.id.changePasswordArrowEditProfile);
        backToProfileArrow = findViewById(R.id.backToProfileArrow);

        editPhotoText = findViewById(R.id.editPhotoEditProfile);
        saveBtn = findViewById(R.id.saveButtonProfileEdit);

        String name = getIntent().getStringExtra("name");
        String surname = getIntent().getStringExtra("surname");
        nameField.setText(name);
        surnameField.setText(surname);


        backToProfileArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAccountActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        editPhotoText.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        saveBtn.setOnClickListener(v -> {
            Map<String, Object> updatedUserData = new HashMap<>();

            String updatedName = nameField.getText().toString().trim();
            if (!updatedName.isEmpty()) {
                updatedUserData.put("name", updatedName);
            }

            String updatedSurname = surnameField.getText().toString().trim();
            if (!updatedSurname.isEmpty()) {
                updatedUserData.put("surname", updatedSurname);
            }

            if (selectedImageUri != null) {
                uploadImageAndSaveUserData(updatedUserData);
            } else {
                saveUserToFirestoreWithMap(updatedUserData);
            }

            Intent intent = new Intent(EditAccountActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void uploadImageAndSaveUserData(Map<String, Object> userData) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/" + selectedImageUri.getLastPathSegment());

        imagesRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
            imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                userData.put("profileImageUrl", imageUrl);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Users").document(userId)
                            .update(userData)
                            .addOnSuccessListener(aVoid -> showToast("User data updated successfully!"))
                            .addOnFailureListener(e -> showToast("Failed to update user data!"));
                }
            });
        }).addOnFailureListener(exception -> {
            showToast("Failed to upload image!");
        });
    }

    private void saveUserToFirestoreWithMap(Map<String, Object> userData) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(userId)
                    .update(userData)
                    .addOnSuccessListener(aVoid -> showToast("User data updated successfully!"))
                    .addOnFailureListener(e -> showToast("Failed to update user data!"));
        } else {
            showToast("Something went wrong.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                showToast("Failed to set image.");
            }
        }
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
                                .into(imageView);
                    }
                }
            });
        }
    }
}

