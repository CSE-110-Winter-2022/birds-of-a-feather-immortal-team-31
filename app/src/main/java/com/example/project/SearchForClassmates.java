package com.example.project;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.Course;
import com.example.project.model.User;

import java.util.ArrayList;
import java.util.List;

public class SearchForClassmates extends AppCompatActivity {

    protected RecyclerView usersRecyclerView;
    protected RecyclerView.LayoutManager usersLayoutManager;
    protected UsersViewAdapter userViewAdapter;



    protected List<Course> courses = new ArrayList<Course>();
    protected Course demo1 = new Course(2018, "Fall", "CSE110");
    protected Course demo2 = new Course(2019, "Winter", "CSE101");

    protected List<User> users = new ArrayList<User>();
    protected User user1 = new User("Luffy","",courses);
    protected User user2 = new User("Zoro","",courses);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_classmates);

        courses.add(demo1);
        courses.add(demo2);

        users.add(user1);
        users.add(user2);

        usersRecyclerView = findViewById(R.id.users_view);

        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        userViewAdapter = new UsersViewAdapter(users);
        usersRecyclerView.setAdapter(userViewAdapter);
    }


    public void onBackButtonClicked(View view) {
        finish();
    }
}