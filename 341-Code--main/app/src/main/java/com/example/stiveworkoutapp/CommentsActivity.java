package com.example.stiveworkoutapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class CommentsActivity extends AppCompatActivity {
    private List<Comment> comments;
    private CommentsAdapter adapter;
    private CommentStorage commentStorage;
    private RecyclerView commentsRecyclerView;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_activity);

        // Get post ID from intent
        postId = getIntent().getStringExtra("post_id");
        if (postId == null) {
            postId = "default_post"; // Fallback ID
        }

        initializeStorage();
        setupRecyclerView();
        setupBottomNavigation();
        setupCommentInput();
    }

    private void initializeStorage() {
        // Initialize comment storage and load saved comments
        commentStorage = new CommentStorage(this);
        comments = commentStorage.loadComments(postId);

        // If no saved comments exist, use the post's initial comments
        if (comments.isEmpty()) {
            Post post = (Post) getIntent().getSerializableExtra("post");
            if (post != null) {
                comments.addAll(post.getComments());
            } else {
                initializeDummyData();
            }
            commentStorage.saveComments(postId, comments);
        }
    }

    private void initializeDummyData() {
        comments.add(new Comment("JohnFit", "Looking strong! 💪"));
        comments.add(new Comment("WorkoutPro", "Great form on those exercises!"));
        comments.add(new Comment("FitnessGuru", "Keep pushing yourself! 🔥"));
        comments.add(new Comment("GymBuddy", "This is inspiring!"));
        comments.add(new Comment("HealthyLife", "Amazing progress!"));

        // Save initial comments
        commentStorage.saveComments(postId, comments);
    }

    private void setupRecyclerView() {
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentsAdapter(comments);
        commentsRecyclerView.setAdapter(adapter);

        // Scroll to the last comment if there are any
        if (!comments.isEmpty()) {
            commentsRecyclerView.scrollToPosition(comments.size() - 1);
        }
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

    private void setupCommentInput() {
        EditText commentInput = findViewById(R.id.comment_input);
        Button addCommentButton = findViewById(R.id.add_comment_button);
        ImageButton backButton = findViewById(R.id.back_button);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        addCommentButton.setOnClickListener(v -> {
            String newCommentText = commentInput.getText().toString().trim();
            if (!newCommentText.isEmpty()) {
                Comment newComment = new Comment("You", newCommentText);
                comments.add(newComment);

                // Update storage
                commentStorage.saveComments(postId, comments);

                // Update adapter with new comments list
                adapter = new CommentsAdapter(comments);
                commentsRecyclerView.setAdapter(adapter);

                // Scroll to new comment
                commentsRecyclerView.scrollToPosition(comments.size() - 1);
                commentInput.setText("");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save comments when activity is paused
        commentStorage.saveComments(postId, comments);
    }
}