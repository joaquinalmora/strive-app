package com.example.stiveworkoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

public class AccountFeed extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_feed);

        // ✅ Link the FAB to the BottomAppBar so it nests in the cradle
        setSupportActionBar(findViewById(R.id.bottom_app_bar));

        // ✅ Setup Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        new BottomNavigationHandler(this, R.id.nav_account_feed)
                .setupBottomNavigation(bottomNav);

        // ✅ FAB Click → CreatePost
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreatePost.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        RecyclerView recyclerView = findViewById(R.id.posts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Post> posts = Arrays.asList(
                new Post("Sami", "Enjoying the sunset!", R.drawable.post1),
                new Post("Ryan", "Had a great workout today!", R.drawable.post2),
                new Post("Joaquin", "Loving this new place!", R.drawable.post3),
                new Post("Jacob", "Throwback to last summer!", R.drawable.share)
        );

        PostsAdapter adapter = new PostsAdapter(posts);
        recyclerView.setAdapter(adapter);

        // Setup navigation buttons
        Button forYouButton = findViewById(R.id.for_you_button);
        Button followingButton = findViewById(R.id.following_button);
        Button friendsButton = findViewById(R.id.friends_button);

        friendsButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, FriendsList.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        followingButton.setOnClickListener(view -> {
            // Currently redirects to same page
            Intent intent = new Intent(this, AccountFeed.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // For You is active, no click handler needed
    }

    private void setupNavigation() {
        TextView forYouText = findViewById(R.id.for_you_text);
        TextView followingText = findViewById(R.id.following_text);
        TextView friendsText = findViewById(R.id.friends_text);

        friendsText.setOnClickListener(v -> {
            Intent intent = new Intent(this, FriendsList.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // For You is active, no need for click handler

        followingText.setOnClickListener(v -> {
            // Currently redirects to same page
            Intent intent = new Intent(this, AccountFeed.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}
