package com.example.stiveworkoutapp;

    import android.content.Context;
    import android.content.SharedPreferences;
    import java.util.HashSet;
    import java.util.Set;

    public class UserLevel {
        private static final String PREFS_NAME = "UserLevelPrefs";
        private static final String KEY_LEVEL = "user_level";
        private static final String KEY_COMPLETED_DAYS = "completed_days";
        private static final int DAYS_NEEDED_FOR_LEVEL = 1;

        private final SharedPreferences prefs;
        private Set<String> completedDays;

        public UserLevel(Context context) {
            this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            this.completedDays = prefs.getStringSet(KEY_COMPLETED_DAYS, new HashSet<>());
        }

        public int getLevel() {
            return prefs.getInt(KEY_LEVEL, 1);
        }

        public void addCompletedDay(String date) {
            // Create a new set with the existing completed days
            Set<String> updatedDays = new HashSet<>(completedDays);
            updatedDays.add(date);

            // Calculate how many new levels should be awarded
            int newLevels = (updatedDays.size() / DAYS_NEEDED_FOR_LEVEL) - (completedDays.size() / DAYS_NEEDED_FOR_LEVEL);

            if (newLevels > 0) {
                int currentLevel = getLevel();
                prefs.edit()
                    .putInt(KEY_LEVEL, currentLevel + 1)
                    .putStringSet(KEY_COMPLETED_DAYS, updatedDays)
                    .commit();
            } else {
                prefs.edit()
                    .putStringSet(KEY_COMPLETED_DAYS, updatedDays)
                    .commit();
            }

            this.completedDays = updatedDays;
        }

        public boolean isDayCompleted(String date) {
            return completedDays.contains(date);
        }
    }