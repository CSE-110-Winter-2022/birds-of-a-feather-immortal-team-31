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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeyViewAdapter extends RecyclerView.Adapter<KeyViewAdapter.ViewHolder>{
    private final List<String> keys;


    public KeyViewAdapter(List<String> keys) {
        super();
        this.keys = keys;
    }

    @NonNull
    @Override
    public KeyViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.layout_person_detail_row, parent, false);

        return new KeyViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyViewAdapter.ViewHolder holder, int position) {
        holder.setKey(this.keys.get(position));
    }

    @Override
    public int getItemCount() {
        return this.keys.size();
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder
    {
        private final TextView keyView;
        private String key;
        private HashMap<String, List<User>> map;

        ViewHolder(View itemView) {
            super(itemView);
            this.keyView = itemView.findViewById(R.id.key_row);
        }

        public void setKey(String key){
            this.key = key;
            this.keyView.setText(key);
        }
    }
}
