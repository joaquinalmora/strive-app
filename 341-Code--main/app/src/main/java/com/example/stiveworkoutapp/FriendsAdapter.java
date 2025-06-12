package com.example.stiveworkoutapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {
    private final List<Friend> friends;
    private final OnFriendClickListener listener;

    public interface OnFriendClickListener {
        void onFriendClick(Friend friend);
    }

    public FriendsAdapter(List<Friend> friends, OnFriendClickListener listener) {
        this.friends = friends;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.friend_item, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.nameTextView.setText(friend.getName());

        // Bind the click listener to the view profile button
        holder.viewProfileButton.setOnClickListener(v -> listener.onFriendClick(friend));

        // Add poke button click listener
        holder.pokeButton.setOnClickListener(v -> {
            Context context = v.getContext();
            String message = friend.getName() + " has been notified";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        });
    }
    @Override
    public int getItemCount() {
        return friends.size();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        Button viewProfileButton;
        Button pokeButton;

        FriendViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.friend_name);
            viewProfileButton = itemView.findViewById(R.id.view_profile_button);
            pokeButton = itemView.findViewById(R.id.poke_button);
        }
    }
}