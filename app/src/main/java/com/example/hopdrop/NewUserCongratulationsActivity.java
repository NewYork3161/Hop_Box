package com.example.hopdrop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewUserCongratulationsActivity extends AppCompatActivity {

    private TextView congratsMessage, emailText;
    private Button sendVerificationButton, returnToLoginButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Handler handler = new Handler();
    private Runnable emailCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_congratulations);

        congratsMessage = findViewById(R.id.congratsMessage);
        emailText = findViewById(R.id.emailText);
        sendVerificationButton = findViewById(R.id.sendVerificationButton);
        returnToLoginButton = findViewById(R.id.returnToLoginButton);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            emailText.setText(currentUser.getEmail());
        }

        returnToLoginButton.setEnabled(false);

        sendVerificationButton.setOnClickListener(v -> {
            if (currentUser != null) {
                currentUser.sendEmailVerification()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Verification email sent!", Toast.LENGTH_SHORT).show();
                            checkEmailVerified();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to send email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        returnToLoginButton.setOnClickListener(v -> {
            startActivity(new Intent(NewUserCongratulationsActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void checkEmailVerified() {
        emailCheckRunnable = new Runnable() {
            @Override
            public void run() {
                currentUser.reload().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (currentUser.isEmailVerified()) {
                            Toast.makeText(NewUserCongratulationsActivity.this, "Email verified!", Toast.LENGTH_SHORT).show();
                            returnToLoginButton.setEnabled(true);
                            congratsMessage.setText("Your email has been verified. You may now return to login.");
                        } else {
                            handler.postDelayed(this, 3000); // check again in 3 seconds
                        }
                    }
                });
            }
        };
        handler.postDelayed(emailCheckRunnable, 3000);
    }
}
