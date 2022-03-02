package com.example.project;

import android.content.Intent;
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
    protected Course demo1 = new Course(2018, "Fall", "CSE110", "tiny");
    protected Course demo2 = new Course(2019, "Winter", "CSE101", "medium");

    protected List<User> users = new ArrayList<User>();
    protected User user1 = new User("Luffy","",courses, 17);
    protected User user2 = new User("Zoro","",courses, 20);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_classmates);

        Intent intent = new Intent(this, MainActivity.class);
        List<User> students = (List<User>) intent.getSerializableExtra("student");

        students.add(new User("1", "URL", courses, 9));

        usersRecyclerView = findViewById(R.id.users_view);

        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        userViewAdapter = new UsersViewAdapter(students);
        usersRecyclerView.setAdapter(userViewAdapter);
    }


    public void onBackButtonClicked(View view) {
        finish();
    }
}