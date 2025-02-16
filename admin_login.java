package com.example.activity_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class admin_login extends AppCompatActivity {

    private EditText emailField2, passwordField2, codewordField;
    private FirebaseAuth auth;

    // Define admin credentials
    private static final String[] ADMIN_EMAILS = {"admin@gmail.com", "admin1@gmail.com"};
    private static final String[] ADMIN_PASSWORDS = {"638701", "220323"};
    private static final String ADMIN_CODEWORD = "The admin";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        // Initialize Firebase instance
        auth = FirebaseAuth.getInstance();

        // Link UI components
        emailField2 = findViewById(R.id.email2);
        passwordField2 = findViewById(R.id.password2);
        codewordField = findViewById(R.id.codeword);

        // Sign-in button click listener
        findViewById(R.id.admin_sign_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAdmin();
            }
        });
    }

    private void loginAdmin() {
        // Get user inputs
        String email = emailField2.getText().toString().trim();
        String password = passwordField2.getText().toString().trim();
        String codeword = codewordField.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(email)) {
            emailField2.setError("Email is required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordField2.setError("Password is required.");
            return;
        }
        if (TextUtils.isEmpty(codeword)) {
            codewordField.setError("Codeword is required.");
            return;
        }

        // Validate against predefined admin credentials
        boolean isAdmin = false;
        for (int i = 0; i < ADMIN_EMAILS.length; i++) {
            if (email.equals(ADMIN_EMAILS[i]) && password.equals(ADMIN_PASSWORDS[i]) && codeword.equals(ADMIN_CODEWORD)) {
                isAdmin = true;
                break;
            }
        }

        if (isAdmin) {
            // Proceed with Firebase authentication
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Login successful
                                Toast.makeText(admin_login.this, "Login successful", Toast.LENGTH_SHORT).show();
                                // Navigate to Admin_Activity
                                Intent intent = new Intent(admin_login.this, Admin_Activity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Firebase authentication failed
                                Toast.makeText(admin_login.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // Invalid admin credentials
            Toast.makeText(admin_login.this, "Invalid credentials or codeword.", Toast.LENGTH_SHORT).show();
        }
    }
}
