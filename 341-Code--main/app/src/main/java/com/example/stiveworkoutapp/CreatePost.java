package com.example.stiveworkoutapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatePost extends AppCompatActivity {

    private EditText descriptionInput;
    private SwitchCompat publicSwitch;
    private SwitchCompat commentsSwitch;
    private Button createPostButton;
    private ImageButton backButton;
    private ImageView workoutImage;
    private LinearLayout addPhotoOption;
    private LinearLayout tagPeopleOption;

    private Uri selectedImageUri = null;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        workoutImage.setImageURI(selectedImageUri);
                        Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);

        setSupportActionBar(findViewById(R.id.bottom_app_bar));
        initializeViews();
        setupClickListeners();
        setupBottomNavigation();
    }

    private void initializeViews() {
        descriptionInput = findViewById(R.id.description_input);
        publicSwitch = findViewById(R.id.public_switch);
        commentsSwitch = findViewById(R.id.comments_switch);
        createPostButton = findViewById(R.id.create_post_button);
        backButton = findViewById(R.id.back_button);
        workoutImage = findViewById(R.id.workout_image);
        addPhotoOption = findViewById(R.id.add_photo_option);
        tagPeopleOption = findViewById(R.id.tag_people_option);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(view -> finish());

        addPhotoOption.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        tagPeopleOption.setOnClickListener(view -> {
            Toast.makeText(this, "Tag people clicked", Toast.LENGTH_SHORT).show();
        });

        createPostButton.setOnClickListener(view -> {
            String description = descriptionInput.getText().toString();

            if (description.isEmpty()) {
                Toast.makeText(this, "Please write a description", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            createPost();
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(view ->
                    Toast.makeText(this, "FAB clicked (you're already here)", Toast.LENGTH_SHORT).show());
        }
    }

    private void createPost() {
        createPostButton.setEnabled(false);
        createPostButton.setText("Creating...");

        createPostButton.postDelayed(() -> {
            String localUri = saveImageToInternalStorage(selectedImageUri);
            if (localUri != null) {

                // 🔽 Save to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("UserInfo", MODE_PRIVATE);
                String saved = prefs.getString("postUris", "");
                List<String> updatedUris = new ArrayList<>();

                if (!saved.isEmpty()) {
                    updatedUris = new ArrayList<>(Arrays.asList(saved.split("\\|\\|")));
                }

                updatedUris.add(localUri); // add the new post URI at the end
                String joined = String.join("||", updatedUris);
                prefs.edit().putString("postUris", joined).apply();
                // 🔼 End save

                // Navigate back to AccountActivity
                Intent intent = new Intent(this, AccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
                createPostButton.setEnabled(true);
                createPostButton.setText("Create Post");
            }
        }, 1000);
    }


    private String saveImageToInternalStorage(Uri sourceUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            if (inputStream == null) return null;

            File dir = new File(getFilesDir(), "posts");
            if (!dir.exists()) dir.mkdirs();

            String fileName = "post_" + System.currentTimeMillis() + ".jpg";
            File outFile = new File(dir, fileName);
            OutputStream outputStream = new FileOutputStream(outFile);

            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return Uri.fromFile(outFile).toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
}
