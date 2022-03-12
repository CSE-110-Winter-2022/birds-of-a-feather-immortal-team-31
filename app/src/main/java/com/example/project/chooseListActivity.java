package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.model.FavoriteUserDatabase;
import com.example.project.model.User;
import com.example.project.model.UserDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chooseListActivity extends AppCompatActivity {

    private UserDatabase db;
    List<User> userList;
    List<String> sessionList;
    public RecyclerView keyRecyclerView;
    public RecyclerView.LayoutManager keyLayoutManager;
    public KeyViewAdapter keyViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);
        db = UserDatabase.singleton(getApplicationContext());
        userList = db.usersDao().getAll();
        for (User i: userList)
        {
            sessionList.add(i.session);
        }

        keyRecyclerView = findViewById(R.id.lists_view);
        keyLayoutManager = new LinearLayoutManager(this);
        keyRecyclerView.setLayoutManager(keyLayoutManager);

        keyViewAdapter = new KeyViewAdapter(sessionList);
        keyRecyclerView.setAdapter(keyViewAdapter);
    }

    public void onConfirmClicked(View view)
    {
        TextView textView = findViewById(R.id.choice);
        String choice = textView.getText().toString();
        if (!sessionList.contains(choice))
        {
            Toast.makeText(getApplicationContext(),"Data does not exist, please enter another name and try again",Toast.LENGTH_LONG).show();
        }
        else
        {
            SharedPreferences preferences = getSharedPreferences("USERCHOICE", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("choice", choice);
            finish();
        }
    }
}