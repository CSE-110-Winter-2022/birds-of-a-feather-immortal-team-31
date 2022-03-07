package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.project.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class resumeActivity extends AppCompatActivity {

    HashMap<String, ArrayList<User>> userLists;
    ArrayList<User> currList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        Intent intent = getIntent();
        userLists = intent.getParcelableExtra("userLists");
        currList = intent.getParcelableExtra("currList");
    }

    public void onNewClicked(View view)
    {
        finish();
    }

    public void onResumeClicked(View view)
    {
        Intent intent = new Intent(this, chooseListActivity.class);
        intent.putExtra("userLists", userLists);
        intent.putExtra("currLists", currList);
        startActivity(intent);
        finish();
    }
}