package com.example.stiveworkoutapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Goals extends AppCompatActivity {
    private TextView tvDate, tvStepsInfo, tvSleepInfo, tvCaloriesInfo;
    private EditText etSteps, etSleep, etCalories;
    private Calendar currentDate = Calendar.getInstance();
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "DailyGoalsPrefs";
    private UserLevel userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goals);
        userLevel = new UserLevel(this);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize views
        tvDate = findViewById(R.id.tvDate);
        ImageView btnPrev = findViewById(R.id.btnPrev);
        ImageView btnNext = findViewById(R.id.btnNext);

        etSteps = findViewById(R.id.etMetricValue);
        tvStepsInfo = findViewById(R.id.tvMetricGoal);
        etSleep = findViewById(R.id.etSleepValue);
        tvSleepInfo = findViewById(R.id.tvSleepGoal);
        etCalories = findViewById(R.id.etCalorieValue);
        tvCaloriesInfo = findViewById(R.id.tvCalorieGoal);

        // Set initial date and load data
        updateDateDisplay();
        loadDailyData();

        // Date navigation
        btnPrev.setOnClickListener(v -> navigateDate(-1));
        btnNext.setOnClickListener(v -> navigateDate(1));

        // Bottom navigation setup
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bottomAppBar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        new BottomNavigationHandler(this, R.id.nav_goals).setupBottomNavigation(bottomNav);

        // FAB setup
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, CreatePost.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // Initialize text watchers
        setupTextWatchers();

        // Setup weekly progress tracker
        setupWeeklyProgress();
    }
    private void checkGoalCompletion() {
        String dateKey = getDateKey(currentDate);

        // Get values with consistent default of "0"
        String stepsStr = sharedPreferences.getString(dateKey + "_steps", "0");
        String sleepStr = sharedPreferences.getString(dateKey + "_sleep", "0");
        String caloriesStr = sharedPreferences.getString(dateKey + "_calories", "0");

        // Parse values safely
        int steps = parseGoalValue(stepsStr);
        int sleep = parseGoalValue(sleepStr);
        int calories = parseGoalValue(caloriesStr);

        // Check if all goals are met
        boolean allGoalsCompleted = steps >= 20000 && sleep >= 8 && calories >= 2000;

        if (allGoalsCompleted && !userLevel.isDayCompleted(dateKey)) {
            userLevel.addCompletedDay(dateKey);
        }
    }
    private void navigateDate(int days) {
        saveDailyData(); // Save current data before changing date
        currentDate.add(Calendar.DAY_OF_MONTH, days);
        updateDateDisplay();
        loadDailyData();
    }

    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMM", Locale.getDefault());
        String dateString;

        if (isToday(currentDate)) {
            dateString = "Today, " + new SimpleDateFormat("dd MMM", Locale.getDefault()).format(currentDate.getTime());
        } else if (isYesterday(currentDate)) {
            dateString = "Yesterday, " + new SimpleDateFormat("dd MMM", Locale.getDefault()).format(currentDate.getTime());
        } else if (isTomorrow(currentDate)) {
            dateString = "Tomorrow, " + new SimpleDateFormat("dd MMM", Locale.getDefault()).format(currentDate.getTime());
        } else {
            dateString = dateFormat.format(currentDate.getTime());
        }

        tvDate.setText(dateString);
    }

    private void setupTextWatchers() {
        etSteps.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String stepsStr = s.toString().isEmpty() ? "0" : s.toString();
                try {
                    int steps = Integer.parseInt(stepsStr);
                    if (steps > 100000) {
                        etSteps.setError("Too many steps! Enter a value less than 100,000.");
                        return;
                    }
                    tvStepsInfo.setText("You've taken " + steps + " steps out of 20,000.");
                    saveDailyData();
                } catch (NumberFormatException e) {
                    etSteps.setError("Invalid number");
                }
                saveDailyData();
            }
        });

        etSleep.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String sleepStr = s.toString().isEmpty() ? "0" : s.toString();
                try {
                    int sleep = Integer.parseInt(sleepStr);
                    if (sleep > 24) {
                        etSleep.setError("You can't sleep more than 24 hours in a day!");
                        return;
                    }
                    tvSleepInfo.setText("You've slept " + sleep + " hours out of 8.");
                    saveDailyData();
                } catch (NumberFormatException e) {
                    etSleep.setError("Invalid number");
                }
                saveDailyData();
            }


        });

        etCalories.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String calorieStr = s.toString().isEmpty() ? "0" : s.toString();
                try {
                    int calories = Integer.parseInt(calorieStr);
                    if (calories > 1000000) {
                        etCalories.setError("That's too many calories burned! Try under 1,000,000.");
                        return;
                    }
                    tvCaloriesInfo.setText("You've burned " + calories + " out of 2000.");
                    saveDailyData();
                } catch (NumberFormatException e) {
                    etCalories.setError("Invalid number");
                }

                String calorie = s.toString().isEmpty() ? "0" : s.toString();
                tvCaloriesInfo.setText("You've burned " + calorie + " out of 2000.");
                saveDailyData();

            }

        });
    }

    private void saveDailyData() {
        String dateKey = getDateKey(currentDate);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Save all three metrics with consistent formatting
        editor.putString(dateKey + "_steps", etSteps.getText().toString().trim());
        editor.putString(dateKey + "_sleep", etSleep.getText().toString().trim());
        editor.putString(dateKey + "_calories", etCalories.getText().toString().trim());

        editor.apply();
        checkGoalCompletion();

        // Refresh weekly progress display
        setupWeeklyProgress();
    }

    private void loadDailyData() {
        String dateKey = getDateKey(currentDate);

        // Load all three metrics with consistent default values
        String steps = sharedPreferences.getString(dateKey + "_steps", "");
        String sleep = sharedPreferences.getString(dateKey + "_sleep", "");
        String calories = sharedPreferences.getString(dateKey + "_calories", "");

        etSteps.setText(steps);
        etSleep.setText(sleep);
        etCalories.setText(calories);

        // Update the info TextViews immediately
        tvStepsInfo.setText("You've taken " + (steps.isEmpty() ? "0" : steps) + " steps out of 20,000.");
        tvSleepInfo.setText("You've slept " + (sleep.isEmpty() ? "0" : sleep) + " hours out of 8.");
        tvCaloriesInfo.setText("You've burned " + (calories.isEmpty() ? "0" : calories) + " out of 2000.");
    }

    private void setupWeeklyProgress() {
        Calendar calendar = Calendar.getInstance();

        // Day labels and icon arrays
        int[] dayTextViews = {
                R.id.tvDay1, R.id.tvDay2, R.id.tvDay3,
                R.id.tvDay4, R.id.tvDay5, R.id.tvDay6, R.id.tvDay7
        };

        int[] stepsIcons = {
                R.id.ivStepsDay1, R.id.ivStepsDay2, R.id.ivStepsDay3,
                R.id.ivStepsDay4, R.id.ivStepsDay5, R.id.ivStepsDay6, R.id.ivStepsDay7
        };

        int[] sleepIcons = {
                R.id.ivSleepDay1, R.id.ivSleepDay2, R.id.ivSleepDay3,
                R.id.ivSleepDay4, R.id.ivSleepDay5, R.id.ivSleepDay6, R.id.ivSleepDay7
        };

        int[] caloriesIcons = {
                R.id.ivCaloriesDay1, R.id.ivCaloriesDay2, R.id.ivCaloriesDay3,
                R.id.ivCaloriesDay4, R.id.ivCaloriesDay5, R.id.ivCaloriesDay6, R.id.ivCaloriesDay7
        };

        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        // Start from 6 days ago
        calendar.add(Calendar.DAY_OF_YEAR, -6);

        for (int i = 0; i < 7; i++) {
            // Set day name
            TextView dayView = findViewById(dayTextViews[i]);
            dayView.setText(dayFormat.format(calendar.getTime()));

            // Check goals for this day
            String dateKey = dateFormat.format(calendar.getTime());
            checkDayGoals(dateKey, stepsIcons[i], sleepIcons[i], caloriesIcons[i]);

            // Move to next day
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    private void checkDayGoals(String dateKey, int stepsIconId, int sleepIconId, int caloriesIconId) {
        // Get values with consistent default of "0"
        String stepsStr = sharedPreferences.getString(dateKey + "_steps", "0");
        String sleepStr = sharedPreferences.getString(dateKey + "_sleep", "0");
        String caloriesStr = sharedPreferences.getString(dateKey + "_calories", "0");

        // Parse values safely
        int steps = parseGoalValue(stepsStr);
        int sleep = parseGoalValue(sleepStr);
        int calories = parseGoalValue(caloriesStr);

        // Update icons - using same logic that works for sleep
        updateGoalIcon(stepsIconId, steps >= 20000);
        updateGoalIcon(sleepIconId, sleep >= 8);
        updateGoalIcon(caloriesIconId, calories >= 2000);
    }

    private int parseGoalValue(String value) {
        try {
            return value.isEmpty() ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void updateGoalIcon(int iconId, boolean goalMet) {
        ImageView icon = findViewById(iconId);
        icon.setVisibility(goalMet ? View.VISIBLE : View.INVISIBLE);
    }

    private String getDateKey(Calendar date) {
        return new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date.getTime());
    }

    private boolean isToday(Calendar date) {
        Calendar today = Calendar.getInstance();
        return date.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                date.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isYesterday(Calendar date) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        return date.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) &&
                date.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR);
    }

    private boolean isTomorrow(Calendar date) {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        return date.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) &&
                date.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR);
    }
}