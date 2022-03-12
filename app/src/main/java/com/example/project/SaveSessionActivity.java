package com.example.project;

import static com.example.project.MainActivity.getMainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.project.model.Course;
import com.example.project.model.User;
import com.example.project.model.UserDatabase;

import java.util.ArrayList;
import java.util.List;

public class SaveSessionActivity extends AppCompatActivity {

    private UserDatabase db;
    private List<User> fellowUsers = new ArrayList<>();

    protected List<Course> courses = new ArrayList<Course>();
    protected Course demo1 = new Course(2020, "spring", "CSE110", "tiny");
    protected Course demo2 = new Course(2021, "winter", "CSE101", "medium");
    protected Course demo3 = new Course(2021, "fall", "CSE2", "small");


    protected User user1 = new User("Luffy","",User.coursesToString( new ArrayList<Course>()), 179876, true, false, "data1");
    protected User user2 = new User("Zoro","",User.coursesToString( new ArrayList<Course>()), 200879, false, false, "data1");
    protected User user3 = new User("Nami","guess", User.coursesToString( new ArrayList<Course>()), 226542, false, false, "data2");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_session);

        db = UserDatabase.singleton(getApplicationContext());

        db.usersDao().insert(user1);
        db.usersDao().insert(user2);
        db.usersDao().insert(user3);

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
        finish();
    }
}