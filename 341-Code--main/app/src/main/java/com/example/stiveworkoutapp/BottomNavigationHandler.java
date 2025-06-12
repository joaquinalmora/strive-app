package com.example.stiveworkoutapp;

import android.app.Activity;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationHandler {
    private final Activity activity;
    private final int selectedItemId;

    public BottomNavigationHandler(Activity activity, int selectedItemId) {
        this.activity = activity;
        this.selectedItemId = selectedItemId;
    }

    public void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setSelectedItemId(selectedItemId);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == selectedItemId) {
                return true;
            }

            Intent intent = null;

            if (itemId == R.id.nav_home) {
                intent = new Intent(activity, MainActivity.class);
            } else if (itemId == R.id.nav_account_feed) {
                intent = new Intent(activity, PostsActivity.class);
            } else if (itemId == R.id.nav_goals) {
                intent = new Intent(activity, Goals.class);// Changed from AccountFeed to PostsActivity
            } else if (itemId == R.id.nav_account) {
                intent = new Intent(activity, AccountActivity.class);
            }

            if (intent != null) {
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }

            return false;
        });
    }
}

