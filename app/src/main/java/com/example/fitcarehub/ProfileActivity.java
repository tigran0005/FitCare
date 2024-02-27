package com.example.fitcarehub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private boolean isLoginOrSignInActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView signout = findViewById(R.id.signout);
        isLoginOrSignInActivity = getIntent().getBooleanExtra("isLoginOrSignInActivity", false);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                isLoginOrSignInActivity = true;
                intent.putExtra("previousActivity", "ProfileActivity");
                intent.putExtra("isLoginOrSignInActivity", isLoginOrSignInActivity);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}
