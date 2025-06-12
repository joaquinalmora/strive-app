package com.example.stiveworkoutapp;

import android.content.Intent;
import android.graphics.ComposeShader;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FriendsList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);

        // Setup bottom navigation
        setSupportActionBar(findViewById(R.id.bottom_app_bar));
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        new BottomNavigationHandler(this, R.id.nav_account_feed)
                .setupBottomNavigation(bottomNav);

        // Setup FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreatePost.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.friends_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create dummy data
        List<Friend> friends = Arrays.asList(
                new Friend("Sami"),
                new Friend("Ryan"),
                new Friend("Joaquin"),
                new Friend("Jacob")
        );

        // Map friend names to profiles
        Map<String, UserProfile> profiles = new HashMap<>();
        profiles.put("Sami", new UserProfile("Sami", "Loves hiking and fitness", 12000, 7, 1800));
        profiles.put("Ryan", new UserProfile("Ryan", "Avid runner and cyclist", 15000, 6, 2000));
        profiles.put("Joaquin", new UserProfile("Joaquin", "Night owl and gamer", 8000, 5, 1500));
        profiles.put("Jacob", new UserProfile("Jacob", "Yoga and meditation enthusiast", 10000, 8, 1700));

        // Set adapter
        FriendsAdapter adapter = new FriendsAdapter(friends, friend -> {
            // Get the selected friend's profile
            UserProfile profile = profiles.get(friend.getName());

            // Start UserProfileActivity with the profile data
        if(profile != null) {
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra("name", profile.getName());
            intent.putExtra("bio", profile.getBio());
            intent.putExtra("steps", profile.getSteps());
            intent.putExtra("sleepHours", profile.getSleepHours());
            intent.putExtra("caloriesBurned", profile.getCaloriesBurned());
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Profile not found", Toast.LENGTH_SHORT).show();
        }
        });
        recyclerView.setAdapter(adapter);

        Button forYouButton = findViewById(R.id.for_you_button);
        Button followingButton = findViewById(R.id.following_button);

        forYouButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, PostsActivity.class);
            startActivity(intent);
        });

        followingButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, PostsActivity.class);
            startActivity(intent);
        });

        // Friends is active, no click handler needed
    }

    private void setupNavigation() {
        TextView forYouText = findViewById(R.id.for_you_text);
        TextView followingText = findViewById(R.id.following_text);
        TextView friendsText = findViewById(R.id.friends_text);

        forYouText.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountFeed.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        followingText.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountFeed.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // Friends text is already active, no need for click handler
    }
}