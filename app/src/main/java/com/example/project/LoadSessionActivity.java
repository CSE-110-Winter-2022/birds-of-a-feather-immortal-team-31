package com.example.project;

import static com.example.project.MainActivity.getMainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.project.model.FavoriteUserDatabase;
import com.example.project.model.User;
import com.example.project.model.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class LoadSessionActivity extends AppCompatActivity {

    private UserDatabase db;
    private List<User> users;
    private List<User> fellowUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_session);

        db = UserDatabase.singleton(getApplicationContext());
        users = db.usersDao().getAll();
        fellowUsers = new ArrayList<>();
        getPreviousSession(users);
    }

    public void getPreviousSession(List<User> userList)
    {
        // Ask for resuming previous sessions or not
        Intent intent = new Intent (this, resumeActivity.class);
        startActivity(intent);

        // get the name of the session chosen by the user
        SharedPreferences preferences = getSharedPreferences("USERCHOICE", Context.MODE_PRIVATE);
        String choice = preferences.getString("choice", null);

        // add the users with corresponding session into fellowUser
        for (User i : userList)
        {
            if (i.getSession().equals(choice))
            {
                fellowUsers.add(i);
            }
        }
        getMainActivity().setFellowUsers(fellowUsers);
    }
}