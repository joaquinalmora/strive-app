package com.example.stiveworkoutapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyActivity extends AppCompatActivity {
    
    private Switch postSharingSwitch;
    private SharedPreferences preferences;
    public static final String PRIVACY_PREFS = "privacyPrefs";
    public static final String SHARING_ENABLED = "sharingEnabled";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        
        preferences = getSharedPreferences(PRIVACY_PREFS, MODE_PRIVATE);
        
        findViewById(R.id.privacy_back_arrow).setOnClickListener(view -> {
            Intent intent = new Intent(PrivacyActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        });
        
        // Initialize post sharing switch
        postSharingSwitch = findViewById(R.id.switch_post_sharing);
        
        // Set the switch state based on saved preferences (default to true)
        boolean sharingEnabled = preferences.getBoolean(SHARING_ENABLED, true);
        postSharingSwitch.setChecked(sharingEnabled);
        
        // Set listener to save preference when changed
        postSharingSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(SHARING_ENABLED, isChecked);
            editor.apply();
        });
    }
}
