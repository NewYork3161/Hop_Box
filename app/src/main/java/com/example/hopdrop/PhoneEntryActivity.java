package com.example.hopdrop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PhoneEntryActivity extends AppCompatActivity {
    private EditText phoneInput;
    private Button nextButton;
    private String firstName, lastName, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_entry);

        phoneInput = findViewById(R.id.phoneInput);
        nextButton = findViewById(R.id.nextButton);

        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        nextButton.setOnClickListener(v -> {
            String phone = phoneInput.getText().toString().trim();

            if (phone.isEmpty()) {
                Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(PhoneEntryActivity.this, LocationEntryActivity.class);
            intent.putExtra("firstName", firstName);
            intent.putExtra("lastName", lastName);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("phone", phone);
            startActivity(intent);
        });
    }
}
