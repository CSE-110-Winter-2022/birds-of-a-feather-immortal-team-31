package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.User;

import java.io.Serializable;
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
        holder.setUser(this.users.get(position));
    }

    public User getUserAtIndex(int i) {
        return users.get(i);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {
        private final TextView userView;
        private User user;

        ViewHolder(View itemView) {
            super(itemView);
            this.userView = itemView.findViewById(R.id.users_row);
            itemView.setOnClickListener(this);
        }

        public void setUser(User user){
            this.user = user;
            this.userView.setText(user.getName());
        }


        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, PersonDetailActivity.class);
            intent.putExtra("user_name", this.user.getName());
            intent.putExtra("user_photoURL", this.user.getPhotoURL());
            intent.putExtra("user_courses", (Serializable) this.user.getCourses());
            intent.putExtra("user_id", String.valueOf(this.user.getId()));
            context.startActivity(intent);
        }


    }
}
