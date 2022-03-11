package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.project.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class resumeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);
        Intent intent = getIntent();
    }

    public void onNewClicked(View view)
    {
        finish();
    }

    public void onResumeClicked(View view)
    {
        Intent intent = new Intent(this, chooseListActivity.class);
        startActivity(intent);
        finish();
    }
}