package com.example.activity_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Accepted extends AppCompatActivity {

    TextView acceptedTextView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted);

        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.ad_home) {
                startActivity(new Intent(Accepted.this, Admin_Activity.class));
                return true;
            }  else if (item.getItemId() == R.id.acc_bot) {
                startActivity(new Intent(Accepted.this, Accepted.class));
                return true;
            }else if (item.getItemId() == R.id.bot_users) {
                startActivity(new Intent(Accepted.this, Users_activity.class));
                return true;
            } else if (item.getItemId() == R.id.drive_up) {
                startActivity(new Intent(Accepted.this, drive_link.class));
                return true;
            }else if (item.getItemId() == R.id.bot1_logout) {
                startActivity(new Intent(Accepted.this, registration_activity.class));
                return true;
            } else {
                Toast.makeText(Accepted.this, "Invalid selection", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        acceptedTextView = findViewById(R.id.acceptedTextView);

        // Firebase reference
        DatabaseReference approvalsReference = FirebaseDatabase.getInstance().getReference("Approvals");
        // Fetch accepted users for all events
        approvalsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder acceptedUsers = new StringBuilder("Accepted Users for All Events:\n\n");

                // Iterate through each event (e.g., Contest, Hackathon, etc.)
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String eventName = eventSnapshot.getKey();

                    acceptedUsers.append("Event: ").append(eventName).append("\n\n");

                    // Iterate through accepted users under each event
                    for (DataSnapshot userSnapshot : eventSnapshot.getChildren()) {
                        String email = userSnapshot.getKey().replace(",", ".");
                        String rollNumber = userSnapshot.getValue(String.class);

                        acceptedUsers.append("  Email: ").append(email).append("\n");
                        acceptedUsers.append("  Roll Number: ").append(rollNumber).append("\n\n");
                    }
                    acceptedUsers.append("\n"); // Separate events with a blank line
                }

                acceptedTextView.setText(acceptedUsers.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Accepted.this, "Failed to retrieve data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
