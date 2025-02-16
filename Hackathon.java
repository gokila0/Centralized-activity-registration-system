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

public class Hackathon extends AppCompatActivity {

    TextView linkTextView;
    Button requestButton;
    DatabaseReference requestsReference, approvalsReference, userReference;

    String userEmail;
    String userRollNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hackathon);

        linkTextView = findViewById(R.id.linkTextView);
        requestButton = findViewById(R.id.request_but);

        // Firebase references
        DatabaseReference eventLinkReference = FirebaseDatabase.getInstance().getReference("EventLinks").child("HackathonLink");
        requestsReference = FirebaseDatabase.getInstance().getReference("Requests").child("Hackathon");
        approvalsReference = FirebaseDatabase.getInstance().getReference("Approvals").child("Hackathon");
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
                            Toast.makeText(Hackathon.this, "Roll number not found. Please complete your profile.", Toast.LENGTH_SHORT).show();
                            requestButton.setEnabled(false);
                        }
                    } else {
                        Toast.makeText(Hackathon.this, "User data not found. Please contact support.", Toast.LENGTH_SHORT).show();
                        requestButton.setEnabled(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Hackathon.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in. Redirecting to login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Hackathon.this, registration_activity.class));
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
                                linkTextView.setText(eventLink);
                                makeLinkClickable(eventLink);
                                requestButton.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Hackathon.this, "Failed to retrieve event link.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    linkTextView.setText("Approval Pending...");
                    requestButton.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Hackathon.this, "Failed to check approval status.", Toast.LENGTH_SHORT).show();
            }
        });

        requestButton.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userRollNumber)) {
                requestsReference.child(userEmail.replace(".", ",")).setValue(userRollNumber)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(Hackathon.this, "Request sent to admin.", Toast.LENGTH_SHORT).show();
                            requestButton.setEnabled(false);
                        })
                        .addOnFailureListener(e -> Toast.makeText(Hackathon.this, "Failed to send request. Try again.", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(Hackathon.this, "Email or Roll Number is missing. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bot_logout) {
                startActivity(new Intent(Hackathon.this, registration_activity.class));
                return true;
            } else if (item.getItemId() == R.id.report) {
                startActivity(new Intent(Hackathon.this, feedback.class));
                return true;
            } else if (item.getItemId() == R.id.home1) {
                startActivity(new Intent(Hackathon.this, MainActivity.class));
                return true;
            } else {
                Toast.makeText(Hackathon.this, "Invalid selection", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void makeLinkClickable(String eventLink) {
        linkTextView.setOnClickListener(v -> {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eventLink));
                startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(Hackathon.this, "Invalid link format.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
