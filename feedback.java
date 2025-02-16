package com.example.activity_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class feedback extends AppCompatActivity {
    private Button buttonSubmit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        buttonSubmit = findViewById(R.id.buttonSubmit);

        BottomNavigationView bnv= findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.bot_logout) {
                    startActivity(new Intent(feedback.this, registration_activity.class));
                    return true;
                } else if (item.getItemId() == R.id.report) {
                    startActivity(new Intent(feedback.this, feedback.class));
                    return true;
                }
                else if (item.getItemId() == R.id.home1) {
                    startActivity(new Intent(feedback.this, MainActivity.class));
                    return true;
                }
                else if (item.getItemId() == R.id.upload) {
                    startActivity(new Intent(feedback.this, drive_update.class));
                    return true;
                }
                else {
                    Toast.makeText(feedback.this, "Intent failed", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String UriText = "mailto:" +      Uri.encode("ecommerceappkec@gmail.com") +"?subject="+
                        Uri.encode("Feedback ") +"by user="+ Uri.encode("");
                Uri uri = Uri.parse(UriText);
                intent.setData(uri);
                startActivity(Intent.createChooser(intent,"send email"));
            }
        });

    }
}