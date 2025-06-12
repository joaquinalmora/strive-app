package com.example.stiveworkoutapp;

import java.io.Serializable;

public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String commentText;

    public Comment(String username, String commentText) {
        this.username = username;
        this.commentText = commentText;
    }

    public String getUsername() {
        return username;
    }

    public String getCommentText() {
        return commentText;
    }
}