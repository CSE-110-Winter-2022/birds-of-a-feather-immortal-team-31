package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

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

public class saveUserActivity extends AppCompatActivity {

    private UserDatabase db;
    List<User> userList;
    List<String> sessionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_user);
        db = UserDatabase.singleton(getApplicationContext());
        userList = db.usersDao().getAll();
        for (User i : userList)
        {
            sessionList.add(i.session);
        }
    }

    public void onDiscardClicked(View view)
    {
        SharedPreferences preferences = getSharedPreferences("DATANAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NAME", "discard");
        finish();
    }

    public void onSaveClicked(View view)
    {
        TextView nameView = findViewById(R.id.name);
        String name = nameView.getText().toString();
        if (sessionList.contains(name))
        {
            Toast.makeText(this, "The name already exists, please enter another name", Toast.LENGTH_LONG).show();
        }
        else if (name.equals("discard"))
        {
            Toast.makeText(this, "discard is a keyword that cannot be used", Toast.LENGTH_LONG).show();
        }
        else
        {
            SharedPreferences preferences = getSharedPreferences("DATANAME", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("NAME", name);
            finish();
        }
    }
}