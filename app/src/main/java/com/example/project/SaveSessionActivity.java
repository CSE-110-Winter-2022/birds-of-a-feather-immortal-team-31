package com.example.project;

import static com.example.project.MainActivity.getMainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.project.model.User;
import com.example.project.model.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class SaveSessionActivity extends AppCompatActivity {

    private UserDatabase db;
    private List<User> users;
    private List<User> fellowUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_session);

        db = UserDatabase.singleton(getApplicationContext());
        users = db.usersDao().getAll();
        fellowUsers = getMainActivity().getFellowUsers();
        saveCurrSession();
    }

    public void saveCurrSession()
    {
        // start of implementation of user story 3 from Yifei Wang
        // After clicks on stop, the users shall be saved into a list.
        Intent intent = new Intent(this, saveUserActivity.class);
        startActivity(intent);

        // getting the name given by the user to this session
        SharedPreferences preferences = getSharedPreferences("DATANAME", Context.MODE_PRIVATE);
        String sessionName = preferences.getString("name", null);

        if (!sessionName.equals("discard"))
        {
            for (User i : fellowUsers)
            {
                db.usersDao().insert(new User(i.name, i.photoURL, i.courses,  i.getId(), i.isWaved(), i.getStar(), sessionName));
            }
        }
    }
}