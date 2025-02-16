package com.example.activity_registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.activity_registration.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    CardView card1,card2,card3,card4,card5,card6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        card1=findViewById(R.id.card1);
        card2=findViewById(R.id.card2);
        card3=findViewById(R.id.card3);
        card4=findViewById(R.id.card4);
        card5=findViewById(R.id.card5);
        card6=findViewById(R.id.card6);

        BottomNavigationView bnv= findViewById(R.id.bottomNavigationView);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.bot_logout) {
                    startActivity(new Intent(MainActivity.this, registration_activity.class));
                    return true;
                } else if (item.getItemId() == R.id.report) {
                    startActivity(new Intent(MainActivity.this, feedback.class));
                    return true;
                }
                else if (item.getItemId() == R.id.home1) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                }
                else if (item.getItemId() == R.id.upload) {
                    startActivity(new Intent(MainActivity.this, drive_update.class));
                    return true;
                }
                else {
                    Toast.makeText(MainActivity.this, "Intent failed", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in1=new Intent(MainActivity.this, Hackathon.class);
                startActivity(in1);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2=new Intent(MainActivity.this, Ideathon.class);
                startActivity(in2);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in3=new Intent(MainActivity.this, Orion.class);
                startActivity(in3);
            }
        });

        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in4=new Intent(MainActivity.this, Mini_Orion.class);
                startActivity(in4);
            }
        });

        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in5=new Intent(MainActivity.this, contest.class);
                startActivity(in5);
            }
        });

        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in6=new Intent(MainActivity.this, Workshops.class);
                startActivity(in6);
            }
        });

    }
}