package com.example.stiveworkoutapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {
    private List<Post> posts;

    public PostsAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.usernameTextView.setText(post.getUsername());
        holder.captionTextView.setText(post.getCaption());
        holder.postImageView.setImageResource(post.getImageResId());
        holder.timeTextView.setText(getTimeAgo(post.getTimestamp()));

        // Set like and comment button click listeners
        holder.likeButton.setOnClickListener(v -> {
            boolean isLiked = v.getTag() != null && (boolean) v.getTag();
            v.setTag(!isLiked);
            ((ImageButton) v).setImageResource(isLiked ?
                    R.drawable.ic_heart_outline : R.drawable.ic_heart_filled);
        });

        holder.commentButton.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, CommentsActivity.class);
            intent.putExtra("post_id", post.getId()); // Use the post's unique ID
            intent.putExtra("post", post);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView captionTextView;
        TextView timeTextView;
        ImageView postImageView;
        ImageView profileImageView;
        ImageButton likeButton;
        ImageButton commentButton;

        PostViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.username_text);
            captionTextView = itemView.findViewById(R.id.caption_text);
            timeTextView = itemView.findViewById(R.id.time_text);
            postImageView = itemView.findViewById(R.id.post_image);
            profileImageView = itemView.findViewById(R.id.profile_image);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);
        }
    }
    private String getTimeAgo(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        long minutes = diff / (60 * 1000);
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + "d ago";
        } else if (hours > 0) {
            return hours + "h ago";
        } else if (minutes > 0) {
            return minutes + "m ago";
        } else {
            return "Just now";
        }
    }
}