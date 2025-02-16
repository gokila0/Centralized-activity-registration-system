package com.example.activity_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Workshops extends AppCompatActivity {

    TextView linkTextView5;
    Button requestButton5;
    DatabaseReference requestsReference, approvalsReference, userReference;

    String userEmail;
    String userRollNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshops);

        linkTextView5 = findViewById(R.id.linkTextView5);
        requestButton5 = findViewById(R.id.request_but5);

        // Firebase references
        DatabaseReference eventLinkReference = FirebaseDatabase.getInstance().getReference("EventLinks").child("WorkshopsLink");
        requestsReference = FirebaseDatabase.getInstance().getReference("Requests").child("Workshops");
        approvalsReference = FirebaseDatabase.getInstance().getReference("Approvals").child("Workshops");
        userReference = FirebaseDatabase.getInstance().getReference("Users");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            userEmail = currentUser.getEmail();
            String userId = currentUser.getUid();

            userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        userRollNumber = snapshot.child("roll_no").getValue(String.class);
                        if (TextUtils.isEmpty(userRollNumber)) {
                            Toast.makeText(Workshops.this, "Roll number not found. Please complete your profile.", Toast.LENGTH_SHORT).show();
                            requestButton5.setEnabled(false);
                        }
                    } else {
                        Toast.makeText(Workshops.this, "User data not found. Please contact support.", Toast.LENGTH_SHORT).show();
                        requestButton5.setEnabled(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Workshops.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in. Redirecting to login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Workshops.this, registration_activity.class));
            finish();
            return;
        }

        approvalsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userEmail.replace(".", ","))) {
                    eventLinkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot linkSnapshot) {
                            String eventLink = linkSnapshot.getValue(String.class);
                            if (!TextUtils.isEmpty(eventLink)) {
                                linkTextView5.setText(eventLink);
                                makeLinkClickable(eventLink);
                                requestButton5.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Workshops.this, "Failed to retrieve event link.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    linkTextView5.setText("Approval Pending...");
                    requestButton5.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Workshops.this, "Failed to check approval status.", Toast.LENGTH_SHORT).show();
            }
        });

        requestButton5.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userRollNumber)) {
                requestsReference.child(userEmail.replace(".", ",")).setValue(userRollNumber)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(Workshops.this, "Request sent to admin.", Toast.LENGTH_SHORT).show();
                            requestButton5.setEnabled(false);
                        })
                        .addOnFailureListener(e -> Toast.makeText(Workshops.this, "Failed to send request. Try again.", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(Workshops.this, "Email or Roll Number is missing. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up BottomNavigationView
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bot_logout) {
                startActivity(new Intent(Workshops.this, registration_activity.class));
                return true;
            } else if (item.getItemId() == R.id.report) {
                startActivity(new Intent(Workshops.this, feedback.class));
                return true;
            } else if (item.getItemId() == R.id.home1) {
                startActivity(new Intent(Workshops.this, MainActivity.class));
                return true;
            } else {
                Toast.makeText(Workshops.this, "Invalid selection", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void makeLinkClickable(String eventLink) {
        linkTextView5.setOnClickListener(v -> {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eventLink));
                startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(Workshops.this, "Invalid link format.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
