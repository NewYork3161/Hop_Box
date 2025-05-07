package com.example.hopdrop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LocationEntryActivity extends AppCompatActivity {
    private EditText cityInput, stateInput;
    private Button registerButton;

    private String firstName, lastName, email, password, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_entry);

        cityInput = findViewById(R.id.cityInput);
        stateInput = findViewById(R.id.stateInput);
        registerButton = findViewById(R.id.registerButton);

        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        phone = getIntent().getStringExtra("phone");

        registerButton.setOnClickListener(v -> {
            String city = cityInput.getText().toString().trim();
            String state = stateInput.getText().toString().trim();

            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("firstName", firstName);
                            userData.put("lastName", lastName);
                            userData.put("email", email);
                            userData.put("phone", phone);
                            userData.put("city", city);
                            userData.put("state", state);

                            FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();
                                        // Navigate to Congratulations screen
                                        Intent intent = new Intent(this, NewUserCongratulationsActivity.class);
                                        startActivity(intent);
                                        finish();
                                    });
                        } else {
                            Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
