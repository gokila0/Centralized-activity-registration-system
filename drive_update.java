package com.example.activity_registration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class drive_update extends AppCompatActivity {
    TextView linkTextViewh2, linkTextViewi2, linkTextViewo2, linkTextViewmo2, linkTextViewc2, linkTextVieww2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_update);

        linkTextViewh2 = findViewById(R.id.linkTextViewh2);
        linkTextViewi2 = findViewById(R.id.linkTextViewi2);
        linkTextViewo2 = findViewById(R.id.linkTextViewo2);
        linkTextViewmo2 = findViewById(R.id.linkTextViewmo2);
        linkTextViewc2 = findViewById(R.id.linkTextViewc2);
        linkTextVieww2 = findViewById(R.id.linkTextVieww2);

        // Initialize Firebase reference
        DatabaseReference eventLinkReference = FirebaseDatabase.getInstance().getReference("driveLink");

        // Retrieve event links and set them to TextViews
        setLinkTextView(eventLinkReference.child("HackathonLink"), linkTextViewh2);
        setLinkTextView(eventLinkReference.child("IdeathonLink"), linkTextViewi2);
        setLinkTextView(eventLinkReference.child("OrionLink"), linkTextViewo2);
        setLinkTextView(eventLinkReference.child("MiniOrionLink"), linkTextViewmo2);
        setLinkTextView(eventLinkReference.child("ContestLink"), linkTextViewc2);
        setLinkTextView(eventLinkReference.child("WorkshopsLink"), linkTextVieww2);

        // Navigation for bottom menu
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bot_logout) {
                startActivity(new Intent(drive_update.this, registration_activity.class));
                return true;
            } else if (item.getItemId() == R.id.report) {
                startActivity(new Intent(drive_update.this, feedback.class));
                return true;
            } else if (item.getItemId() == R.id.home1) {
                startActivity(new Intent(drive_update.this, MainActivity.class));
                return true;
            }
            else if (item.getItemId() == R.id.upload) {
                startActivity(new Intent(drive_update.this, drive_update.class));
                return true;
            }else {
                Toast.makeText(drive_update.this, "Invalid selection", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private void setLinkTextView(DatabaseReference eventLinkRef, TextView linkTextView) {
        eventLinkRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String link = task.getResult().getValue(String.class);
                if (link != null) {
                    linkTextView.setText(link);
                    makeLinkClickable(linkTextView, link);
                } else {
                    Toast.makeText(drive_update.this, "Link not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(drive_update.this, "Failed to load link.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeLinkClickable(TextView linkTextView, String eventLink) {
        linkTextView.setOnClickListener(v -> {
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(eventLink));
                startActivity(browserIntent);
            } catch (Exception e) {
                Toast.makeText(drive_update.this, "Invalid link format.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
