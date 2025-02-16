package com.example.activity_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class contest extends AppCompatActivity {

    TextView linkTextView2;
    Button requestButton2;
    DatabaseReference requestsReference, approvalsReference, userReference, eventLinkReference;

    String userEmail;
    String userRollNumber;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest);

        linkTextView2 = findViewById(R.id.linkTextView2);
        requestButton2 = findViewById(R.id.request_but2);

        // Firebase references
        eventLinkReference = FirebaseDatabase.getInstance().getReference("EventLinks").child("ContestLink");
        requestsReference = FirebaseDatabase.getInstance().getReference("Requests").child("Contest");
        approvalsReference = FirebaseDatabase.getInstance().getReference("Approvals").child("Contest");
        userReference = FirebaseDatabase.getInstance().getReference("Users");

        // Fetch current user information
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userEmail = currentUser.getEmail();
            String userId = currentUser.getUid();

            // Fetch user roll number
            fetchUserRollNumber(userId);
        } else {
            Toast.makeText(this, "User not logged in. Redirecting to login.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(contest.this, registration_activity.class));
            finish();
            return;
        }

        // Listen for approval changes
        listenForApprovalChanges();

        // Set request button click listener
        requestButton2.setOnClickListener(v -> sendParticipationRequest());

        // Set up BottomNavigationView
        setUpBottomNavigation();
    }

    private void fetchUserRollNumber(String userId) {
        userReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userRollNumber = snapshot.child("roll_no").getValue(String.class);
                    if (TextUtils.isEmpty(userRollNumber)) {
                        Toast.makeText(contest.this, "Roll number not found. Please complete your profile.", Toast.LENGTH_SHORT).show();
                        requestButton2.setEnabled(false);
                    }
                } else {
                    Toast.makeText(contest.this, "User data not found. Please contact support.", Toast.LENGTH_SHORT).show();
                    requestButton2.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(contest.this, "Failed to fetch user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listenForApprovalChanges() {
        approvalsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userEmail.replace(".", ","))) {
                    // Approved user: Fetch and display event link
                    fetchAndDisplayEventLink();
                } else {
                    // Approval pending
                    linkTextView2.setText("Approval Pending...");
                    requestButton2.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(contest.this, "Failed to check approval status.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchAndDisplayEventLink() {
        eventLinkReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String eventLink = snapshot.getValue(String.class);
                if (!TextUtils.isEmpty(eventLink)) {
                    linkTextView2.setText(eventLink);
                    makeLinkClickable(eventLink);
                    requestButton2.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(contest.this, "Failed to retrieve event link.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendParticipationRequest() {
        if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userRollNumber)) {
            String sanitizedEmail = userEmail.replace(".", ",");
            requestsReference.child(sanitizedEmail).setValue(userRollNumber)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(contest.this, "Request sent to admin.", Toast.LENGTH_SHORT).show();
                        requestButton2.setEnabled(false);
                    })
                    .addOnFailureListener(e -> Toast.makeText(contest.this, "Failed to send request. Try again.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(contest.this, "Email or Roll Number is missing. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpBottomNavigation() {
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bot_logout) {
                startActivity(new Intent(contest.this, registration_activity.class));
                return true;
            } else if (item.getItemId() == R.id.report) {
                startActivity(new Intent(contest.this, feedback.class));
                return true;
            } else if (item.getItemId() == R.id.home1) {
                startActivity(new Intent(contest.this, MainActivity.class));
                return true;
            } else {
                Toast.makeText(contest.this, "Invalid selection", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void makeLinkClickable(String eventLink) {
        linkTextView2.setOnClickListener(v -> {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eventLink));
                startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(contest.this, "Invalid link format.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
