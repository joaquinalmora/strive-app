package com.example.stiveworkoutapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String caption;
    private String id;
    private int imageResId;
    private long timestamp;
    private List<Comment> comments;

    public Post(String username, String caption, int imageResId) {
        this.username = username;
        this.caption = caption;
        this.imageResId = imageResId;
        this.timestamp = System.currentTimeMillis();
        this.id = username + "_" + timestamp;
        this.comments = new ArrayList<>(); // Initialize empty list
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getCaption() {
        return caption;
    }

    public int getImageResId() {
        return imageResId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void initializeComments(CommentStorage storage) {
        // Load comments from storage first
        List<Comment> storedComments = storage.loadComments(id);

        // Only generate dummy comments if no stored comments exist
        if (storedComments.isEmpty()) {
            comments = generateDummyComments();
            storage.saveComments(id, comments);
        } else {
            comments = storedComments;
        }
    }

    private List<Comment> generateDummyComments() {
        List<Comment> dummyComments = new ArrayList<>();

        // Fixed set of comments for each post based on username
        if (username.equals("Sami")) {
            dummyComments.add(new Comment("GymPro", "405 is insane! 🏋️"));
            dummyComments.add(new Comment("FitnessFanatic", "Beast mode activated 💪"));
            dummyComments.add(new Comment("WorkoutBuddy", "Show us the form!"));
        } else if (username.equals("Jacob")) {
            dummyComments.add(new Comment("CardioKing", "Get those miles in! 🏃"));
            dummyComments.add(new Comment("RunnerLife", "Cardio is life"));
            dummyComments.add(new Comment("FitFam", "Keep pushing!"));
        } else if (username.equals("Ryan")) {
            dummyComments.add(new Comment("SunsetRunner", "Perfect timing ⭐"));
            dummyComments.add(new Comment("WorkoutPro", "Beautiful view"));
            dummyComments.add(new Comment("FitnessJourney", "Nature and fitness 🌅"));
        } else if (username.equals("Joaquin")) {
            dummyComments.add(new Comment("ClassWarrior", "Let's crush it! 💪"));
            dummyComments.add(new Comment("GymLife", "Ready to work"));
            dummyComments.add(new Comment("FitFam", "Class time energy"));
        } else if (username.equals("FitnessGuru")) {
            dummyComments.add(new Comment("HIITMaster", "HIIT is the way! 🔥"));
            dummyComments.add(new Comment("WorkoutPro", "Great session"));
            dummyComments.add(new Comment("FitLife", "Love the intensity"));
        } else if (username.equals("GymLife")) {
            dummyComments.add(new Comment("MondayMotivation", "Sunday gains! 💪"));
            dummyComments.add(new Comment("FitnessFreak", "No days off"));
            dummyComments.add(new Comment("GymBro", "Keep grinding"));
        } else if (username.equals("WorkoutPro")) {
            dummyComments.add(new Comment("RecoveryExpert", "Recovery is key! 🧘"));
            dummyComments.add(new Comment("StretchMaster", "Flexibility gains"));
            dummyComments.add(new Comment("WellnessGuru", "Important work"));
        } else if (username.equals("HealthyHabits")) {
            dummyComments.add(new Comment("MorningRunner", "Beautiful start! 🌅"));
            dummyComments.add(new Comment("RunLife", "Morning cardio is the best"));
            dummyComments.add(new Comment("FitnessFanatic", "Great way to start the day"));
        } else {
            dummyComments.add(new Comment("FitnessLover", "Great work! 💪"));
            dummyComments.add(new Comment("GymLife", "Keep it up!"));
            dummyComments.add(new Comment("WorkoutPro", "Looking strong"));
        }

        return dummyComments;
    }
}