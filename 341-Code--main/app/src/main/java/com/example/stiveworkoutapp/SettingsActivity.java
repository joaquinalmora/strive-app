package com.example.stiveworkoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stiveworkoutapp.BottomNavigationHandler;
import com.example.stiveworkoutapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Top bar views
        ImageView backArrow = findViewById(R.id.back_arrow);
        ImageView friendsIcon = findViewById(R.id.friends_icon);

        // Navigate back to AccountActivity
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to FriendsList
        friendsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, FriendsList.class);
                startActivity(intent);
            }
        });

        // Setting rows listeners:

        // Personal Information row
        LinearLayout personalInfoLayout = findViewById(R.id.personalInfoLayout);
        personalInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, PersonalInfoActivity.class);
                startActivity(intent);
            }
        });

        // Privacy row
        LinearLayout privacyLayout = findViewById(R.id.privacyLayout);
        privacyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, PrivacyActivity.class);
                startActivity(intent);
            }
        });

        // Avatar row
        LinearLayout avatarLayout = findViewById(R.id.avatarLayout);
        avatarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AvatarActivity.class);
                startActivity(intent);
            }
        });

        // Bottom Navigation setup
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        new BottomNavigationHandler(this, R.id.nav_account)
                .setupBottomNavigation(bottomNav);
    }
}