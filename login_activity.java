package com.example.activity_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity {

    // Declare UI components
    private EditText emailField, passwordField;
    private Button signInButton;
    private TextView signUpLink;

    // Firebase Authentication
    private FirebaseAuth auth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Link UI components
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        signInButton = findViewById(R.id.button_sign_in);
        signUpLink = findViewById(R.id.Signup);
        TextView adsignup=findViewById(R.id.Signup1);

        // Sign-in button click listener
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(); // Call the loginUser method
            }
        });

        adsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2=new Intent(getApplicationContext(), admin_login.class);
                startActivity(in2);
            }
        });

        // Sign-up link click listener
        signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Registration Activity
                Intent intent = new Intent(login_activity.this, registration_activity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        // Get user input
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Email is required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Password is required.");
            return;
        }

        // Authenticate with Firebase
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful
                            Toast.makeText(login_activity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            // Navigate to home screen or another activity
                            Intent intent = new Intent(login_activity.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Close the login activity
                        } else {
                            // Login failed
                            Toast.makeText(login_activity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
