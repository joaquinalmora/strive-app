package com.example.stiveworkoutapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import java.util.Calendar;
import java.util.regex.Pattern;

public class PersonalInfoActivity extends AppCompatActivity {
    
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private TextView dobTextView;
    private Spinner genderSpinner;
    private AutoCompleteTextView addressEditText;
    private EditText weightEditText;
    private EditText heightEditText;
    private Button saveButton;
    private LinearLayout dobContainer;
    private SharedPreferences sharedPreferences;
    private boolean isPhoneFormatting = false;
    
    // Use the same constant as in AccountActivity
    private static final String PREFS_NAME = "UserInfo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // Initialize SharedPreferences with the same name as AccountActivity
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        
        // Initialize UI elements
        initializeViews();
        setupInputValidation();
        loadSavedData();
        setupListeners();
    }
    
    private void initializeViews() {
        usernameEditText = findViewById(R.id.edit_username);
        emailEditText = findViewById(R.id.edit_email);
        phoneEditText = findViewById(R.id.edit_phone);
        dobTextView = findViewById(R.id.text_dob);
        genderSpinner = findViewById(R.id.spinner_gender);
        addressEditText = findViewById(R.id.edit_address);
        weightEditText = findViewById(R.id.edit_weight);
        heightEditText = findViewById(R.id.edit_height);
        saveButton = findViewById(R.id.save_button);
        dobContainer = findViewById(R.id.dob_container);
        
        // Setup gender spinner with customized adapter
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, // Custom layout for items
                getResources().getStringArray(R.array.gender_options));
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item); // Custom dropdown layout
        genderSpinner.setAdapter(adapter);
    }
    
    private void setupInputValidation() {
        // Full Name validation - only letters and spaces allowed
        usernameEditText.setFilters(new InputFilter[] {
            new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, 
                                          Spanned dest, int dstart, int dend) {
                    for (int i = start; i < end; i++) {
                        if (!Character.isLetter(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i))) {
                            Toast.makeText(PersonalInfoActivity.this, 
                                "Name can only contain letters", Toast.LENGTH_SHORT).show();
                            return "";
                        }
                    }
                    return null;
                }
            }
        });
        
        // Weight validation - only numbers and decimal point
        weightEditText.setFilters(new InputFilter[] {
            new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, 
                                          Spanned dest, int dstart, int dend) {
                    for (int i = start; i < end; i++) {
                        if (!Character.isDigit(source.charAt(i)) && source.charAt(i) != '.') {
                            Toast.makeText(PersonalInfoActivity.this, 
                                "Weight can only contain numbers", Toast.LENGTH_SHORT).show();
                            return "";
                        }
                    }
                    
                    // Check for multiple decimal points
                    String result = dest.toString().substring(0, dstart) + 
                                   source.toString().substring(start, end) +
                                   dest.toString().substring(dend);
                    
                    if (result.contains(".")) {
                        if (result.indexOf(".") != result.lastIndexOf(".")) {
                            Toast.makeText(PersonalInfoActivity.this,
                                "Weight can only have one decimal point", Toast.LENGTH_SHORT).show();
                            return "";
                        }
                    }
                    return null;
                }
            }
        });
        
        // Height validation - only numbers and decimal point
        heightEditText.setFilters(new InputFilter[] {
            new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, 
                                          Spanned dest, int dstart, int dend) {
                    for (int i = start; i < end; i++) {
                        if (!Character.isDigit(source.charAt(i)) && source.charAt(i) != '.') {
                            Toast.makeText(PersonalInfoActivity.this, 
                                "Height can only contain numbers", Toast.LENGTH_SHORT).show();
                            return "";
                        }
                    }
                    
                    // Check for multiple decimal points
                    String result = dest.toString().substring(0, dstart) + 
                                   source.toString().substring(start, end) +
                                   dest.toString().substring(dend);
                    
                    if (result.contains(".")) {
                        if (result.indexOf(".") != result.lastIndexOf(".")) {
                            Toast.makeText(PersonalInfoActivity.this,
                                "Height can only have one decimal point", Toast.LENGTH_SHORT).show();
                            return "";
                        }
                    }
                    return null;
                }
            }
        });
        
        // Auto-capitalize first letter of each word in name
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() > 0) {
                    boolean capitalize = true;
                    StringBuilder formatted = new StringBuilder();
                    
                    for (int i = 0; i < text.length(); i++) {
                        char c = text.charAt(i);
                        if (Character.isWhitespace(c)) {
                            capitalize = true;
                            formatted.append(c);
                        } else if (capitalize) {
                            formatted.append(Character.toUpperCase(c));
                            capitalize = false;
                        } else {
                            formatted.append(Character.toLowerCase(c));
                        }
                    }
                    
                    if (!text.equals(formatted.toString())) {
                        usernameEditText.removeTextChangedListener(this);
                        usernameEditText.setText(formatted);
                        usernameEditText.setSelection(formatted.length());
                        usernameEditText.addTextChangedListener(this);
                    }
                }
            }
        });
        
        // Phone number formatting
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isPhoneFormatting) return;
                
                isPhoneFormatting = true;
                
                // Remove all non-digit characters
                String digits = s.toString().replaceAll("\\D", "");
                
                // Format to xxx-xxx-xxxx
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < Math.min(digits.length(), 10); i++) {
                    if (i == 3 || i == 6) {
                        formatted.append('-');
                    }
                    formatted.append(digits.charAt(i));
                }
                
                phoneEditText.setText(formatted);
                phoneEditText.setSelection(formatted.length());
                
                isPhoneFormatting = false;
            }
        });
    }
    
    private void loadSavedData() {
        // Load existing data from SharedPreferences
        usernameEditText.setText(sharedPreferences.getString("username", ""));
        emailEditText.setText(sharedPreferences.getString("email", ""));
        phoneEditText.setText(sharedPreferences.getString("phone", ""));
        dobTextView.setText(sharedPreferences.getString("dob", "Select date of birth"));
        
        // Set gender spinner selection
        String savedGender = sharedPreferences.getString("gender", "");
        ArrayAdapter adapter = (ArrayAdapter) genderSpinner.getAdapter();
        if (!savedGender.isEmpty()) {
            int position = adapter.getPosition(savedGender);
            if (position >= 0) {
                genderSpinner.setSelection(position);
            }
        }
        
        addressEditText.setText(sharedPreferences.getString("address", ""));
        weightEditText.setText(sharedPreferences.getString("weight", ""));
        heightEditText.setText(sharedPreferences.getString("height", ""));
    }
    
    private void setupListeners() {
        // Date of birth picker with year jumping capability
        dobContainer.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                PersonalInfoActivity.this,
                (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    String dateString = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dobTextView.setText(dateString);
                    dobTextView.setTextColor(getResources().getColor(android.R.color.white));
                },
                year, month, day
            );
            
            // Configure the DatePicker to show the year selector directly
            try {
                DatePicker datePicker = datePickerDialog.getDatePicker();
                datePicker.setCalendarViewShown(false);
                datePicker.setSpinnersShown(true);
                
                // Set maximum date to current date (can't select future dates)
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            } catch (Exception e) {
                // Fallback if customization fails
                e.printStackTrace();
            }
            
            datePickerDialog.show();
        });

        // Save button click listener
        saveButton.setOnClickListener(view -> {
            if (validateInputs()) {
                saveUserData();
                Toast.makeText(PersonalInfoActivity.this, "Information saved", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(PersonalInfoActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.personal_info_back_arrow).setOnClickListener(view -> {
            Intent intent = new Intent(PersonalInfoActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        });
    }
    
    private boolean validateInputs() {
        // Email validation
        String email = emailEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            emailEditText.requestFocus();
            return false;
        }
        
        // Phone number validation
        String phone = phoneEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            String digitsOnly = phone.replaceAll("\\D", "");
            if (digitsOnly.length() != 10) {
                Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
                phoneEditText.requestFocus();
                return false;
            }
        }
        
        // Weight validation
        String weight = weightEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(weight)) {
            try {
                double weightValue = Double.parseDouble(weight);
                if (weightValue <= 0 || weightValue > 1000) {
                    Toast.makeText(this, "Please enter a valid weight less than 1000", Toast.LENGTH_SHORT).show();
                    weightEditText.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid weight", Toast.LENGTH_SHORT).show();
                weightEditText.requestFocus();
                return false;
            }
        }
        
        // Height validation
        String height = heightEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(height)) {
            try {
                double heightValue = Double.parseDouble(height);
                if (heightValue <= 0 || heightValue > 108) { // 108 inches = 9 feet
                    Toast.makeText(this, "Please enter a valid height less than 9 feet", Toast.LENGTH_SHORT).show();
                    heightEditText.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid height", Toast.LENGTH_SHORT).show();
                heightEditText.requestFocus();
                return false;
            }
        }
        
        // All validations passed
        return true;
    }

    private void saveUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save all user information to SharedPreferences
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        // Save username and email (the ones that will be displayed in the profile)
        editor.putString("username", username);
        editor.putString("email", email);

        // Save other fields
        editor.putString("phone", phoneEditText.getText().toString().trim());
        editor.putString("dob", dobTextView.getText().toString().trim());
        editor.putString("gender", genderSpinner.getSelectedItem().toString());
        editor.putString("address", addressEditText.getText().toString().trim());
        editor.putString("weight", weightEditText.getText().toString().trim());
        editor.putString("height", heightEditText.getText().toString().trim());

        editor.apply();
    }

    @Override
    public void onBackPressed() {
        // Navigate back to Settings Activity
        Intent intent = new Intent(PersonalInfoActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
}