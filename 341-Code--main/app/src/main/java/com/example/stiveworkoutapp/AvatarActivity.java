package com.example.stiveworkoutapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AvatarActivity extends AppCompatActivity {
    
    // Array of avatar resource IDs to choose from
    private final int[] avatarOptions = {
            R.drawable.profile_icon, 
            R.drawable.avatar1, 
            R.drawable.avatar2, 
            R.drawable.avatar3, 
            R.drawable.avatar4,
            R.drawable.avatar5
    };
    
    private int selectedAvatarIndex = 0;
    private FrameLayout[] avatarFrames;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int ORANGE_COLOR = 0xFFFF5722; // Orange color for selection
    private static final int GRAY_COLOR = 0xFF444444;   // Gray color for non-selected
    
    private boolean isCustomImageSelected = false;
    private Uri customImageUri = null;
    private String currentPhotoPath;

    // Activity result launcher for image picking
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                if (imageUri != null) {
                    try {
                        handleSelectedImage(imageUri);
                    } catch (IOException e) {
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        }
    );
    
    // Permission launcher for Android 11+ compatibility
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    isGranted -> {
                        if (isGranted) {
                            openImagePicker();
                        } else {
                            Toast.makeText(this,
                                    "Permission denied. Cannot select image.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        // Get current avatar selection from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        boolean isCustomImage = sharedPreferences.getBoolean("isCustomImage", false);
        
        // Initialize avatar frames array
        avatarFrames = new FrameLayout[6];
        
        if (isCustomImage) {
            // Custom image is selected
            String customImagePath = sharedPreferences.getString("customImagePath", null);
            if (customImagePath != null) {
                isCustomImageSelected = true;
                customImageUri = Uri.parse(customImagePath);
                
                // Find the custom image preview
                ImageView customImageView = findViewById(R.id.custom_image_preview);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), customImageUri);
                    customImageView.setImageBitmap(bitmap);
                    customImageView.setVisibility(View.VISIBLE);
                    
                    // Show the custom image container
                    findViewById(R.id.custom_image_container).setVisibility(View.VISIBLE);
                    
                    // Add orange border to custom image container
                    FrameLayout customImageFrame = findViewById(R.id.custom_image_frame);
                    GradientDrawable shape = new GradientDrawable();
                    shape.setShape(GradientDrawable.RECTANGLE);
                    shape.setCornerRadius(16);
                    shape.setColor(GRAY_COLOR);
                    shape.setStroke(5, ORANGE_COLOR);
                    customImageFrame.setBackground(shape);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // A default avatar is selected
            selectedAvatarIndex = sharedPreferences.getInt("avatarIndex", 0);
        }
        
        // Initialize avatar grid images
        initializeAvatarGrid();
        
        // Setup click listener for the back arrow
        findViewById(R.id.avatar_back_arrow).setOnClickListener(v -> finish());
        
        // Setup upload button
        Button uploadButton = findViewById(R.id.upload_image_button);
        uploadButton.setOnClickListener(v -> checkPermissionAndOpenPicker());
        
        // Setup save button
        Button saveButton = findViewById(R.id.save_avatar_button);
        saveButton.setOnClickListener(v -> saveAvatarSelection());
    }
    
    private void initializeAvatarGrid() {
        // Initialize avatar frames
        avatarFrames[0] = findViewById(R.id.avatar_frame_1);
        avatarFrames[1] = findViewById(R.id.avatar_frame_2);
        avatarFrames[2] = findViewById(R.id.avatar_frame_3);
        avatarFrames[3] = findViewById(R.id.avatar_frame_4);
        avatarFrames[4] = findViewById(R.id.avatar_frame_5);
        avatarFrames[5] = findViewById(R.id.avatar_frame_6);
        
        // Setup avatar option image views
        ImageView[] avatarImageViews = new ImageView[6];
        avatarImageViews[0] = findViewById(R.id.avatar_option_1);
        avatarImageViews[1] = findViewById(R.id.avatar_option_2);
        avatarImageViews[2] = findViewById(R.id.avatar_option_3);
        avatarImageViews[3] = findViewById(R.id.avatar_option_4);
        avatarImageViews[4] = findViewById(R.id.avatar_option_5);
        avatarImageViews[5] = findViewById(R.id.avatar_option_6);
        
        // Set avatar images and click listeners
        for (int i = 0; i < avatarImageViews.length; i++) {
            final int index = i;
            avatarImageViews[i].setImageResource(avatarOptions[i]);
            avatarImageViews[i].setOnClickListener(v -> selectAvatar(index));
            
            // Initialize frame backgrounds
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(16);
            shape.setColor(GRAY_COLOR);
            shape.setStroke(3, GRAY_COLOR);
            avatarFrames[i].setBackground(shape);
        }
        
        // Mark the currently selected avatar if it's a default one
        if (!isCustomImageSelected && selectedAvatarIndex >= 0 && selectedAvatarIndex < avatarOptions.length) {
            // Highlight the selected avatar
            highlightSelectedAvatar(selectedAvatarIndex);
        }
    }
    
    private void selectAvatar(int index) {
        // Reset all frames first
        for (int i = 0; i < avatarFrames.length; i++) {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(16);
            shape.setColor(GRAY_COLOR);
            shape.setStroke(3, GRAY_COLOR);
            avatarFrames[i].setBackground(shape);
        }
        
        // Reset custom image selection
        isCustomImageSelected = false;
        customImageUri = null;
        
        // Hide custom image container
        findViewById(R.id.custom_image_container).setVisibility(View.GONE);
        
        // Update selected avatar index
        selectedAvatarIndex = index;
        
        // Highlight the selected avatar
        highlightSelectedAvatar(index);
    }
    
    private void highlightSelectedAvatar(int index) {
        // First ensure index is valid
        if (index < 0 || index >= avatarFrames.length) {
            return;
        }
        
        // Apply orange border to the selected avatar
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(16);
        shape.setColor(GRAY_COLOR);
        shape.setStroke(5, ORANGE_COLOR);
        
        avatarFrames[index].setBackground(shape);
    }
    
    private void checkPermissionAndOpenPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) 
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                openImagePicker();
            }
        } else {
            openImagePicker();
        }
    }
    
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }
    
    private void handleSelectedImage(Uri imageUri) throws IOException {
        // Reset all frames first
        for (int i = 0; i < avatarFrames.length; i++) {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(16);
            shape.setColor(GRAY_COLOR);
            shape.setStroke(3, GRAY_COLOR);
            avatarFrames[i].setBackground(shape);
        }
        
        // Make the custom image selected
        isCustomImageSelected = true;
        customImageUri = imageUri;
        
        // Get permission to read the image persistently - only for API 19 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
            } catch (SecurityException e) {
                // Handle the exception if permission can't be taken
                Toast.makeText(this, "Failed to get persistent permission", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        
        // Update the image preview
        ImageView customImageView = findViewById(R.id.custom_image_preview);
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), customImageUri);
        customImageView.setImageBitmap(bitmap);
        
        // Show the custom image container with orange border
        View container = findViewById(R.id.custom_image_container);
        container.setVisibility(View.VISIBLE);
        
        // Add orange border to the custom image frame
        FrameLayout customImageFrame = findViewById(R.id.custom_image_frame);
        if (customImageFrame != null) {
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(16);
            shape.setColor(GRAY_COLOR);
            shape.setStroke(5, ORANGE_COLOR);
            customImageFrame.setBackground(shape);
        }
    }
    
    private void saveAvatarSelection() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        
        if (isCustomImageSelected && customImageUri != null) {
            // Save custom image settings
            editor.putBoolean("isCustomImage", true);
            editor.putString("customImagePath", customImageUri.toString());
            editor.putInt("avatarIndex", -1); // -1 indicates custom image
        } else {
            // Save default avatar settings
            editor.putBoolean("isCustomImage", false);
            editor.putInt("avatarIndex", selectedAvatarIndex);
            editor.putInt("avatarResourceId", avatarOptions[selectedAvatarIndex]);
        }
        
        editor.apply();
        
        // Set result to indicate the avatar was changed
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        
        Toast.makeText(this, "Profile picture saved", Toast.LENGTH_SHORT).show();
        finish();
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this,
                        "Permission denied. Cannot select image.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
        
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}

