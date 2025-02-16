package com.example.activity_registration;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class drive_link extends AppCompatActivity {

    // Declare views for each event
    EditText eventTextView1, eventTextView2, eventTextView3, eventTextView4, eventTextView5, eventTextView6;
    Button sendButton1, deleteButton1, sendButton2, deleteButton2, sendButton3, deleteButton3,
            sendButton4, deleteButton4, sendButton5, deleteButton5, sendButton6, deleteButton6;

    // Firebase References for each event link
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_link);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("driveLink");

        // Initialize Views and set up listeners
        initializeEventViews();
        setButtonListeners(eventTextView1, sendButton1, deleteButton1, "HackathonLink");
        setButtonListeners(eventTextView2, sendButton2, deleteButton2, "IdeathonLink");
        setButtonListeners(eventTextView3, sendButton3, deleteButton3, "OrionLink");
        setButtonListeners(eventTextView4, sendButton4, deleteButton4, "MiniOrionLink");
        setButtonListeners(eventTextView5, sendButton5, deleteButton5, "ContestLink");
        setButtonListeners(eventTextView6, sendButton6, deleteButton6, "WorkshopsLink");

        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.ad_home) {
                startActivity(new Intent(drive_link.this, Admin_Activity.class));
                return true;
            } else if (item.getItemId() == R.id.acc_bot) {
                startActivity(new Intent(drive_link.this, Accepted.class));
                return true;
            } else if (item.getItemId() == R.id.bot_users) {
                startActivity(new Intent(drive_link.this, Users_activity.class));
                return true;
            } else if (item.getItemId() == R.id.drive_up) {
                startActivity(new Intent(drive_link.this, drive_link.class));
                return true;
            } else if (item.getItemId() == R.id.bot1_logout) {
                startActivity(new Intent(drive_link.this, registration_activity.class));
                return true;
            } else {
                Toast.makeText(drive_link.this, "Invalid selection", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void initializeEventViews() {
        // Initialize all Event TextViews and Buttons
        eventTextView1 = findViewById(R.id.eventTextView1);
        sendButton1 = findViewById(R.id.sendButton1);
        deleteButton1 = findViewById(R.id.deleteButton1);

        eventTextView2 = findViewById(R.id.eventTextView2);
        sendButton2 = findViewById(R.id.sendButton2);
        deleteButton2 = findViewById(R.id.deleteButton2);

        eventTextView3 = findViewById(R.id.eventTextView3);
        sendButton3 = findViewById(R.id.sendButton3);
        deleteButton3 = findViewById(R.id.deleteButton3);

        eventTextView4 = findViewById(R.id.eventTextView4);
        sendButton4 = findViewById(R.id.sendButton4);
        deleteButton4 = findViewById(R.id.deleteButton4);

        eventTextView5 = findViewById(R.id.eventTextView5);
        sendButton5 = findViewById(R.id.sendButton5);
        deleteButton5 = findViewById(R.id.deleteButton5);

        eventTextView6 = findViewById(R.id.eventTextView6);
        sendButton6 = findViewById(R.id.sendButton6);
        deleteButton6 = findViewById(R.id.deleteButton6);
    }

    private void setButtonListeners(EditText eventTextView, Button sendButton, Button deleteButton, String databaseKey) {
        DatabaseReference eventLinkRef = databaseReference.child(databaseKey);

        // Handle Send Button Click
        sendButton.setOnClickListener(v -> {
            String link = eventTextView.getText().toString().trim();
            if (!link.isEmpty()) {
                eventLinkRef.setValue(link)
                        .addOnSuccessListener(aVoid -> Toast.makeText(drive_link.this, "Link updated successfully.", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(drive_link.this, "Failed to update link.", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(drive_link.this, "Please enter a valid link.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Delete Button Click
        deleteButton.setOnClickListener(v -> {
            eventLinkRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        eventTextView.setText("");
                        Toast.makeText(drive_link.this, "Link deleted successfully.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(drive_link.this, "Failed to delete link.", Toast.LENGTH_SHORT).show());
        });
    }
}
