package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class saveUserActivity extends AppCompatActivity {

    HashMap <String, ArrayList<Integer>> userLists;
    ArrayList<Integer> currList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_user);
        Intent intent = getIntent();
        userLists = intent.getParcelableExtra("userLists");
        currList = intent.getIntegerArrayListExtra("currList");
    }

    public void onDiscardClicked(View view)
    {
        finish();
    }

    public void onSaveClicked(View view)
    {
        SharedPreferences preferences = getSharedPreferences("DATANAME", Context.MODE_PRIVATE);

        TextView nameView = findViewById(R.id.name);

        SharedPreferences.Editor editor = preferences.edit();
        String name = nameView.toString();

        while (userLists.containsKey(name))
        {
            Toast.makeText(this, "The name already exists, please enter another name", Toast.LENGTH_LONG).show();
            name = nameView.toString();
        }
        editor.putString("NAME", name);
        userLists.put(name, currList);
        finish();
    }
}