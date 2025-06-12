package com.example.stiveworkoutapp;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CommentStorage {
    private static final String PREF_NAME = "CommentsPrefs";
    private final SharedPreferences prefs;
    private final Gson gson;

    public CommentStorage(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void saveComments(String postId, List<Comment> comments) {
        String jsonComments = gson.toJson(comments);
        prefs.edit().putString(postId, jsonComments).apply();
    }

    public List<Comment> loadComments(String postId) {
        String jsonComments = prefs.getString(postId, null);
        if (jsonComments == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Comment>>() {}.getType();
        return gson.fromJson(jsonComments, type);
    }
}