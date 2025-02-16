package com.example.activity_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Users_activity extends AppCompatActivity {

    private DatabaseReference reference;
    private TextView userDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.ad_home) {
                startActivity(new Intent(Users_activity.this, Admin_Activity.class));
                return true;
            }  else if (item.getItemId() == R.id.acc_bot) {
                startActivity(new Intent(Users_activity.this, Accepted.class));
                return true;
            }else if (item.getItemId() == R.id.bot_users) {
                startActivity(new Intent(Users_activity.this, Users_activity.class));
                return true;
            }else if (item.getItemId() == R.id.drive_up) {
                startActivity(new Intent(Users_activity.this, drive_link.class));
                return true;
            } else if (item.getItemId() == R.id.bot1_logout) {
                startActivity(new Intent(Users_activity.this, registration_activity.class));
                return true;
            } else {
                Toast.makeText(Users_activity.this, "Invalid selection", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        userDataView = findViewById(R.id.user_data_view); // Ensure this matches an ID in your XML layout

        // Initialize Firebase Database reference
        reference = FirebaseDatabase.getInstance().getReference("Users");

        // Fetch and display user data
        fetchUserData();
    }

    private void fetchUserData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder data = new StringBuilder();

                // Iterate through all children under "Users"
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String name = userSnapshot.child("name").getValue(String.class);
                    String email = userSnapshot.child("email").getValue(String.class);
                    String rollNo = userSnapshot.child("roll_no").getValue(String.class);
                    String mobileNo = userSnapshot.child("mob_no").getValue(String.class);

                    // Format data for display
                    data.append("Name: ").append(name).append("\n");
                    data.append("Email: ").append(email).append("\n");
                    data.append("Roll No: ").append(rollNo).append("\n");
                    data.append("Mobile No: ").append(mobileNo).append("\n\n");
                }

                // Display data in the TextView
                userDataView.setText(data.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
                Toast.makeText(Users_activity.this, "Failed to retrieve data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AdminActivity", "Database error: " + error.getMessage());
            }
        });
    }
}