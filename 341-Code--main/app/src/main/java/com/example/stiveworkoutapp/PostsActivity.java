package com.example.stiveworkoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stiveworkoutapp.Post;
import com.example.stiveworkoutapp.PostsAdapter;
import com.example.stiveworkoutapp.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends AppCompatActivity {
    private List<Post> posts;
    private PostsAdapter postsAdapter;
    private RecyclerView postsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_activity);

        // Initialize the posts list
        posts = new ArrayList<>();

        // Initialize RecyclerView
        postsRecyclerView = findViewById(R.id.postsRecyclerView);
        postsRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        postsAdapter = new PostsAdapter(posts);
        postsRecyclerView.setAdapter(postsAdapter);

        // Setup bottom navigation
        setupBottomNavigation();

        Button forYouButton = findViewById(R.id.for_you_button);
        Button followingButton = findViewById(R.id.following_button);
        Button friendsButton = findViewById(R.id.friends_button);

        forYouButton.setOnClickListener(v -> {
            initializeForYouPosts();
            updateButtonStates(forYouButton, followingButton, friendsButton);
        });

        followingButton.setOnClickListener(v -> {
            initializeFollowingPosts();
            updateButtonStates(followingButton, forYouButton, friendsButton);
        });

        friendsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FriendsList.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // Initialize with "For You" posts by default
        initializeForYouPosts();
        updateButtonStates(forYouButton, followingButton, friendsButton);
    }

    private void setupBottomNavigation() {
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        if (bottomAppBar != null) {
            setSupportActionBar(bottomAppBar);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            new BottomNavigationHandler(this, R.id.nav_account_feed)
                    .setupBottomNavigation(bottomNav);
        }
    }

    private void updateButtonStates(Button activeButton, Button... inactiveButtons) {
        activeButton.setAlpha(1.0f);
        for (Button button : inactiveButtons) {
            button.setAlpha(0.5f);
        }
    }

    private void initializeForYouPosts() {
        posts.clear();
        CommentStorage storage = new CommentStorage(this);

        Post post1 = new Post("FitnessGuru", "HIIT session done ✅", R.drawable.post8);
        post1.initializeComments(storage);
        posts.add(post1);

        Post post2 = new Post("GymLife", "Sunday motivation", R.drawable.post9);
        post2.initializeComments(storage);
        posts.add(post2);

        Post post3 = new Post("WorkoutPro", "Recovery day stretches", R.drawable.post10);
        post3.initializeComments(storage);
        posts.add(post3);

        Post post4 = new Post("HealthyHabits", "Morning run views", R.drawable.post11);
        post4.initializeComments(storage);
        posts.add(post4);

        postsAdapter.notifyDataSetChanged();
    }

    private void initializeFollowingPosts() {
        posts.clear();
        CommentStorage storage = new CommentStorage(this);

        Post post1 = new Post("Sami", "Unreal push day incoming (i bench 405).", R.drawable.post7);
        post1.initializeComments(storage);
        posts.add(post1);

        Post post2 = new Post("Jacob", "CARDIOOOO BABYYYYY!", R.drawable.post5);
        post2.initializeComments(storage);
        posts.add(post2);

        Post post3 = new Post("Ryan", "Sunset Workout...", R.drawable.post6);
        post3.initializeComments(storage);
        posts.add(post3);

        Post post4 = new Post("Joaquin", "Ready for class!", R.drawable.post4);
        post4.initializeComments(storage);
        posts.add(post4);

        postsAdapter.notifyDataSetChanged();
    }
}