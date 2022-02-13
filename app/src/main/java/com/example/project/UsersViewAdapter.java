package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.User;

import java.util.List;

public class UsersViewAdapter extends RecyclerView.Adapter<UsersViewAdapter.ViewHolder>{
    private final List<User> users;

    public UsersViewAdapter(List<User> users) {
        super();
        this.users = users;
    }

    @NonNull
    @Override
    public UsersViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_person_detail_row, parent, false);

        return new UsersViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewAdapter.ViewHolder holder, int position) {
        String userName = this.users.get(position).getName();
        String text = userName;
        holder.userView.setText(text);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
    {
        private final TextView userView;

        ViewHolder(View itemView) {
            super(itemView);
            this.userView = itemView.findViewById(R.id.users_row);
        }
    }
}