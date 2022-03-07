package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.project.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class saveUserActivity extends AppCompatActivity {

    HashMap <String, List<User>> userLists;
    ArrayList<User> currList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_user);
        Intent intent = getIntent();
        userLists = intent.getParcelableExtra("userLists");
        currList = intent.getParcelableExtra("currList");
    }

    public void onDiscardClicked(View view)
    {
        finish();
    }

    public void onSaveClicked(View view)
    {
        TextView nameView = findViewById(R.id.name);
        String name = nameView.getText().toString();
        userLists.put("name", currList);
        finish();
    }
}