package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.project.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class chooseListActivity extends AppCompatActivity {

    HashMap<String, ArrayList<User>> userLists;
    ArrayList<User> currList;
    public RecyclerView usersRecyclerView;
    public RecyclerView.LayoutManager usersLayoutManager;
    public UsersViewAdapter userViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list)
        Intent intent = getIntent();
        userLists = intent.getParcelableExtra("userLists");
        currList = intent.getParcelableExtra("currList");
    }
}