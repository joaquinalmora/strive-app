package com.example.stiveworkoutapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {
    private UserLevel userLevel;
    private static final int AVATAR_REQUEST_CODE = 100;
    private ImageView profileImageView;

    private static final String PREFS_NAME = "UserInfo";
    private static final String KEY_POST_URIS = "postUris";
    private static final String TAG = "AccountActivity";

    private TextView usernameTextView;
    private TextView emailTextView;

    // Friend data
    private List<Friend> friends;
    private Map<String, UserProfile> friendProfiles;

    // Activity result launcher for avatar selection
    private final ActivityResultLauncher<Intent> avatarLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Avatar was changed, reload the profile image
                    loadProfileImage();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        userLevel = new UserLevel(this);

        // Initialize profile image view and text views
        profileImageView = findViewById(R.id.profile_icon);
        usernameTextView = findViewById(R.id.username_text);
        emailTextView = findViewById(R.id.gmail_text);

        // Set click listener on profile image to open AvatarActivity
        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, AvatarActivity.class);
            avatarLauncher.launch(intent);
        });

        // Load the saved avatar
        loadProfileImage();

        // Load user profile information
        loadUserProfileInfo();

        // Update the level display
        TextView lvlText = findViewById(R.id.lvl_text);
        lvlText.setText(userLevel.getLevel() + "\nlvl");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        new BottomNavigationHandler(this, R.id.nav_account)
                .setupBottomNavigation(bottomNav);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreatePost.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // Settings button click handler
        findViewById(R.id.settings_icon).setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        // Friends button click handler
        findViewById(R.id.friends_icon).setOnClickListener(view -> {
            Intent intent = new Intent(this, FriendsList.class);
            startActivity(intent);
        });

        // Load friend data
        loadFriendData();

        // Setup friend row click listeners
        setupFriendRowClickListeners();

        // Update the friends count display
        updateFriendsCountText();
    }

    /**
     * Loads and displays the user profile information (username and email)
     * from SharedPreferences
     */
    private void loadUserProfileInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Get saved username and email
        String username = sharedPreferences.getString("username", "username");
        String email = sharedPreferences.getString("email", "username@gmail.com");

        // Update UI with the saved information
        if (!username.isEmpty()) {
            usernameTextView.setText(username);
        }

        if (!email.isEmpty()) {
            emailTextView.setText(email);
        }
    }

    private void loadFriendData() {
        // Initialize with the same dummy data as in FriendsList
        friends = Arrays.asList(
                new Friend("Sami"),
                new Friend("Ryan"),
                new Friend("Joaquin"),
                new Friend("Jacob")
        );

        // Map friend names to profiles (same as in FriendsList)
        friendProfiles = new HashMap<>();
        friendProfiles.put("Sami", new UserProfile("Sami", "Loves hiking and fitness", 12000, 7, 1800));
        friendProfiles.put("Ryan", new UserProfile("Ryan", "Avid runner and cyclist", 15000, 6, 2000));
        friendProfiles.put("Joaquin", new UserProfile("Joaquin", "Night owl and gamer", 8000, 5, 1500));
        friendProfiles.put("Jacob", new UserProfile("Jacob", "Yoga and meditation enthusiast", 10000, 8, 1700));

        // Update UI with top 3 friends
        updateTopFriendsUI();
    }

    private void updateTopFriendsUI() {
        // Get references to the friend rows
        LinearLayout friendRow1 = findViewById(R.id.friend_row_1);
        LinearLayout friendRow2 = findViewById(R.id.friend_row_2);
        LinearLayout friendRow3 = findViewById(R.id.friend_row_3);

        // Get references to the text views in each row
        TextView friendName1 = (TextView) ((LinearLayout) friendRow1).getChildAt(0);
        TextView friendName2 = (TextView) ((LinearLayout) friendRow2).getChildAt(0);
        TextView friendName3 = (TextView) ((LinearLayout) friendRow3).getChildAt(0);

        // Update with top 3 friends (if available)
        int friendsCount = Math.min(friends.size(), 3);
        if (friendsCount > 0) friendName1.setText(friends.get(0).getName());
        if (friendsCount > 1) friendName2.setText(friends.get(1).getName());
        if (friendsCount > 2) friendName3.setText(friends.get(2).getName());
    }

    private void setupFriendRowClickListeners() {
        LinearLayout friendRow1 = findViewById(R.id.friend_row_1);
        LinearLayout friendRow2 = findViewById(R.id.friend_row_2);
        LinearLayout friendRow3 = findViewById(R.id.friend_row_3);

        // Set up click listeners for each friend row
        setupFriendRowClick(friendRow1, 0);
        setupFriendRowClick(friendRow2, 1);
        setupFriendRowClick(friendRow3, 2);
    }

    private void setupFriendRowClick(LinearLayout friendRow, int index) {
        friendRow.setOnClickListener(v -> {
            if (friends != null && index < friends.size()) {
                String friendName = friends.get(index).getName();
                UserProfile profile = friendProfiles.get(friendName);

                if (profile != null) {
                    Intent intent = new Intent(this, UserProfileActivity.class);
                    intent.putExtra("name", profile.getName());
                    intent.putExtra("bio", profile.getBio());
                    intent.putExtra("steps", profile.getSteps());
                    intent.putExtra("sleepHours", profile.getSleepHours());
                    intent.putExtra("caloriesBurned", profile.getCaloriesBurned());
                    startActivity(intent);
                }
            }
        });
    }

    private void updateFriendsCountText() {
        // Update friends count text
        TextView friendsCountText = findViewById(R.id.friends_count_text);
        if (friends != null) {
            friendsCountText.setText(friends.size() + "\nfriends");
        }
    }

    private void loadProfileImage() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isCustomImage = sharedPreferences.getBoolean("isCustomImage", false);

        if (isCustomImage) {
            String customImagePath = sharedPreferences.getString("customImagePath", null);
            if (customImagePath != null) {
                try {
                    Uri imageUri = Uri.parse(customImagePath);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    profileImageView.setImageBitmap(bitmap);
                } catch (IOException | SecurityException e) {
                    profileImageView.setImageResource(R.drawable.profile_icon);
                    e.printStackTrace();
                }
            }
        } else {
            int avatarResourceId = sharedPreferences.getInt("avatarResourceId", R.drawable.profile_icon);
            profileImageView.setImageResource(avatarResourceId);
        }
    }

    private void saveImageUris(List<String> uris) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String joined = String.join("||", uris);
        prefs.edit().putString(KEY_POST_URIS, joined).apply();
        Log.d(TAG, "Saved URIs: " + joined);
    }

    private List<String> loadImageUris() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String saved = prefs.getString(KEY_POST_URIS, "");
        if (saved.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(saved.split("\\|\\|")));
    }

    public void openPostsActivity(View view) {
        startActivity(new Intent(this, PostsActivity.class));
    }

    private void updateYourPostsSection() {
        SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String saved = prefs.getString("postUris", "");
        List<String> uris = saved.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(saved.split("\\|\\")));

        // Show only most recent 3 posts, reverse the list
        List<String> recent = new ArrayList<>();
        for (int i = uris.size() - 1; i >= 0 && recent.size() < 3; i--) {
            recent.add(uris.get(i));
        }

        List<ImageView> imageViews = Arrays.asList(
                findViewById(R.id.post_image_1),
                findViewById(R.id.post_image_2),
                findViewById(R.id.post_image_3)
        );

        int i = 0;
        for (; i < recent.size(); i++) {
            imageViews.get(i).setImageURI(Uri.parse(recent.get(i)));
        }

        // Fill remaining with fallback images if needed
        int[] fallbacks = {R.drawable.post1, R.drawable.post2, R.drawable.post3};
        for (int j = 0; i < 3 && j < fallbacks.length; i++, j++) {
            imageViews.get(i).setImageResource(fallbacks[j]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView lvlText = findViewById(R.id.lvl_text);
        lvlText.setText(userLevel.getLevel() + "\nlvl");
        loadProfileImage();
        updateYourPostsSection();
        loadFriendData(); // Reload friend data when returning to this activity
        loadUserProfileInfo(); // Reload user profile info when returning to the activity
    }
}
