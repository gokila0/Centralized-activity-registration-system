package com.example.activity_registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_Activity extends AppCompatActivity {

    // Event Links: EditTexts and Buttons
    EditText eventTextView1, eventTextView2, eventTextView3, eventTextView4, eventTextView5, eventTextView6;
    Button sendButton1, deleteButton1, sendButton2, deleteButton2, sendButton3, deleteButton3,
            sendButton4, deleteButton4, sendButton5, deleteButton5, sendButton6, deleteButton6;

    // Firebase References
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // ListViews and Adapters for Requests
    ListView requestsListView, requestsListView2, requestsListView3, requestsListView4, requestsListView5, requestsListView6;
    ArrayList<String> hackathonRequestsList, ideathonRequestsList, orionRequestsList, miniOrionRequestsList, contestRequestsList, workshopsRequestsList;
    ArrayAdapter<String> hackathonAdapter, ideathonAdapter, orionAdapter, miniOrionAdapter, contestAdapter, workshopsAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("EventLinks");

        // Initialize Event Views and Buttons
        initializeEventViews();

        // Set up Event Link CRUD listeners
        setButtonListeners(eventTextView1, sendButton1, deleteButton1, "HackathonLink");
        setButtonListeners(eventTextView2, sendButton2, deleteButton2, "IdeathonLink");
        setButtonListeners(eventTextView3, sendButton3, deleteButton3, "OrionLink");
        setButtonListeners(eventTextView4, sendButton4, deleteButton4, "MiniOrionLink");
        setButtonListeners(eventTextView5, sendButton5, deleteButton5, "ContestLink");
        setButtonListeners(eventTextView6, sendButton6, deleteButton6, "WorkshopsLink");

        // Setup ListViews
        setupListViews();

        // Fetch requests for each event
        fetchEventRequests("Hackathon", requestsListView, hackathonRequestsList, hackathonAdapter);
        fetchEventRequests("Ideathon", requestsListView2, ideathonRequestsList, ideathonAdapter);
        fetchEventRequests("Orion", requestsListView3, orionRequestsList, orionAdapter);
        fetchEventRequests("MiniOrion", requestsListView4, miniOrionRequestsList, miniOrionAdapter);
        fetchEventRequests("Contest", requestsListView5, contestRequestsList, contestAdapter);
        fetchEventRequests("Workshops", requestsListView6, workshopsRequestsList, workshopsAdapter);

        // BottomNavigationView Setup
        BottomNavigationView bnv = findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.ad_home) {
                startActivity(new Intent(Admin_Activity.this, Admin_Activity.class));
                return true;
            }  else if (item.getItemId() == R.id.acc_bot) {
                startActivity(new Intent(Admin_Activity.this, Accepted.class));
                return true;
            }else if (item.getItemId() == R.id.bot_users) {
                startActivity(new Intent(Admin_Activity.this, Users_activity.class));
                return true;
            }else if (item.getItemId() == R.id.drive_up) {
                startActivity(new Intent(Admin_Activity.this, drive_link.class));
                return true;
            } else if (item.getItemId() == R.id.bot1_logout) {
                startActivity(new Intent(Admin_Activity.this, registration_activity.class));
                return true;
            } else {
                Toast.makeText(Admin_Activity.this, "Invalid selection", Toast.LENGTH_SHORT).show();
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

        sendButton.setOnClickListener(v -> {
            String link = eventTextView.getText().toString().trim();
            if (!link.isEmpty()) {
                eventLinkRef.setValue(link)
                        .addOnSuccessListener(aVoid -> Toast.makeText(Admin_Activity.this, "Link updated successfully.", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(Admin_Activity.this, "Failed to update link.", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(Admin_Activity.this, "Please enter a valid link.", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            eventLinkRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        eventTextView.setText("");
                        Toast.makeText(Admin_Activity.this, "Link deleted successfully.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(Admin_Activity.this, "Failed to delete link.", Toast.LENGTH_SHORT).show());
        });

        eventLinkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link = snapshot.getValue(String.class);
                if (link != null) {
                    eventTextView.setText(link);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Admin_Activity.this, "Failed to load link.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListViews() {
        requestsListView = findViewById(R.id.requestsListView);
        hackathonRequestsList = new ArrayList<>();
        hackathonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, hackathonRequestsList);
        requestsListView.setAdapter(hackathonAdapter);

        requestsListView2 = findViewById(R.id.requestsListView2);
        ideathonRequestsList = new ArrayList<>();
        ideathonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ideathonRequestsList);
        requestsListView2.setAdapter(ideathonAdapter);

        requestsListView3 = findViewById(R.id.requestsListView3);
        orionRequestsList = new ArrayList<>();
        orionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orionRequestsList);
        requestsListView3.setAdapter(orionAdapter);

        requestsListView4 = findViewById(R.id.requestsListView4);
        miniOrionRequestsList = new ArrayList<>();
        miniOrionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, miniOrionRequestsList);
        requestsListView4.setAdapter(miniOrionAdapter);

        requestsListView5 = findViewById(R.id.requestsListView5);
        contestRequestsList = new ArrayList<>();
        contestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contestRequestsList);
        requestsListView5.setAdapter(contestAdapter);

        requestsListView6 = findViewById(R.id.requestsListView6);
        workshopsRequestsList = new ArrayList<>();
        workshopsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, workshopsRequestsList);
        requestsListView6.setAdapter(workshopsAdapter);
    }

    private void fetchEventRequests(String eventName, ListView listView, ArrayList<String> requestList, ArrayAdapter<String> adapter) {
        DatabaseReference eventRequestsRef = firebaseDatabase.getReference("Requests").child(eventName);
        DatabaseReference eventApprovalsRef = firebaseDatabase.getReference("Approvals").child(eventName);

        eventRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                requestList.clear();
                for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                    String emailKey = requestSnapshot.getKey();
                    String rollNumber = requestSnapshot.getValue(String.class);

                    if (emailKey != null && rollNumber != null) {
                        String emailDisplay = emailKey.replace(",", ".");
                        String displayText = eventName + ": " + emailDisplay + " (" + rollNumber + ")";
                        if (!requestList.contains(displayText)) {
                            requestList.add(displayText);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Admin_Activity.this, "Failed to load requests for " + eventName, Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = requestList.get(position);
            String[] parts = selectedItem.split(": ");
            String emailAndRoll = parts[1];

            String email = emailAndRoll.split(" ")[0];
            String rollNumber = emailAndRoll.split("\\(")[1].replace(")", "");

            new AlertDialog.Builder(Admin_Activity.this)
                    .setTitle("Handle Request for " + eventName)
                    .setMessage("Approve or Reject: " + email)
                    .setPositiveButton("Approve", (dialog, which) ->
                            approveRequest(eventRequestsRef, eventApprovalsRef, email, rollNumber))
                    .setNegativeButton("Reject", (dialog, which) ->
                            rejectRequest(eventRequestsRef, email))
                    .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void approveRequest(DatabaseReference eventRequestsRef, DatabaseReference eventApprovalsRef, String email, String rollNumber) {
        String emailKey = email.replace(".", ",");
        eventApprovalsRef.child(emailKey).setValue(rollNumber)
                .addOnSuccessListener(aVoid -> eventRequestsRef.child(emailKey).removeValue()
                        .addOnSuccessListener(aVoid1 -> Toast.makeText(Admin_Activity.this, "Request approved.", Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e -> Toast.makeText(Admin_Activity.this, "Failed to approve request.", Toast.LENGTH_SHORT).show());
    }

    private void rejectRequest(DatabaseReference eventRequestsRef, String email) {
        String emailKey = email.replace(".", ",");
        eventRequestsRef.child(emailKey).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(Admin_Activity.this, "Request rejected.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(Admin_Activity.this, "Failed to reject request.", Toast.LENGTH_SHORT).show());
    }
}
