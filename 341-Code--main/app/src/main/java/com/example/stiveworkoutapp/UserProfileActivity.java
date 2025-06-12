package com.example.stiveworkoutapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    private List<Post> userPosts;
    private PostsAdapter postsAdapter;
    private RecyclerView postsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Get the user profile data from the intent
        String name = getIntent().getStringExtra("name");
        String bio = getIntent().getStringExtra("bio");
        int steps = getIntent().getIntExtra("steps", 0);
        int sleepHours = getIntent().getIntExtra("sleepHours", 0);
        int caloriesBurned = getIntent().getIntExtra("caloriesBurned", 0);

        // Set the data to the views
        TextView tvName = findViewById(R.id.username_text);
        TextView tvSteps = findViewById(R.id.steps_text);
        TextView tvSleep = findViewById(R.id.sleep_text);
        TextView tvCalories = findViewById(R.id.calories_text);

        tvName.setText(name);
        tvSteps.setText("Steps: " + steps);
        tvSleep.setText("Sleep: " + sleepHours + " hours");
        tvCalories.setText("Calories: " + caloriesBurned);

        // Initialize RecyclerView for posts
        userPosts = new ArrayList<>();
        postsRecyclerView = findViewById(R.id.profile_posts_recycler_view);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsAdapter = new PostsAdapter(userPosts);
        postsRecyclerView.setAdapter(postsAdapter);

        // Initialize posts based on username
        initializeUserPosts(name);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        new BottomNavigationHandler(this, R.id.nav_account_feed)
                .setupBottomNavigation(bottomNav);
                
        // Setup back arrow functionality
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(view -> {
            // Go back to previous activity
            finish();
        });
    }
    private void initializeUserPosts(String username) {
        userPosts.clear();
        switch (username) {
            case "Sami":
                userPosts.add(new Post("Sami", "Unreal push day incoming (i bench 405).", R.drawable.post7));
                break;
            case "Jacob":
                userPosts.add(new Post("Jacob", "CARDIOOOO BABYYYYY!", R.drawable.post5));
                break;
            case "Ryan":
                userPosts.add(new Post("Ryan", "Sunset Workout...", R.drawable.post6));
                break;
            case "Joaquin":
                userPosts.add(new Post("Joaquin", "Ready for class!", R.drawable.post4));
                break;
        }
        postsAdapter.notifyDataSetChanged();
    }
}