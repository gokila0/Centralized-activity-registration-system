package com.example.activity_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registration_activity extends AppCompatActivity {

    private EditText nameField, emailField, passwordField, rollNoField, mobileNoField;
    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration); // Ensure this matches your XML file name

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Users");

        // Link UI components to variables
        nameField = findViewById(R.id.name);
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        rollNoField = findViewById(R.id.roll_no);
        mobileNoField = findViewById(R.id.mobile_num);

        View buttonReg = findViewById(R.id.button_reg); // Registration button
        View signIn = findViewById(R.id.sign_in); // Sign-in TextView


        // Set listener for Registration button
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        // Set listener for Sign-in TextView
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration_activity.this, login_activity.class);
                startActivity(intent);
            }
        });

    }




    private void registerUser() {
        // Get input values
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String rollNo = rollNoField.getText().toString().trim();
        String mobileNo = mobileNoField.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                TextUtils.isEmpty(rollNo) || TextUtils.isEmpty(mobileNo)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Firebase Authentication to register user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Store additional user details in Firebase Realtime Database
                    String userId = auth.getCurrentUser().getUid();
                    Users user = new Users(name, email, rollNo, mobileNo);

                    reference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(registration_activity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(registration_activity.this, login_activity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(registration_activity.this, "Failed to save user data: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(registration_activity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
