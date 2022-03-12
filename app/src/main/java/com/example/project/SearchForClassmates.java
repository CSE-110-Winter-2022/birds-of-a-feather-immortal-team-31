package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import com.example.project.model.Course;
import com.example.project.model.User;

import java.util.ArrayList;
import java.util.List;

public class SearchForClassmates extends AppCompatActivity {

    protected RecyclerView usersRecyclerView;
    protected RecyclerView.LayoutManager usersLayoutManager;
    protected UsersViewAdapter userViewAdapter;



    protected List<Course> courses = new ArrayList<Course>();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_classmates);

        Intent intent = new Intent(this, MainActivity.class);
        List<User> students = (List<User>) intent.getSerializableExtra("student");

        //students.add(new User("1", "URL",User.coursesToString(courses), 9, false, false));

        usersRecyclerView = findViewById(R.id.users_view);

        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        userViewAdapter = new UsersViewAdapter(ApplicationProvider.getApplicationContext(),students);
        usersRecyclerView.setAdapter(userViewAdapter);
    }


    public void onBackButtonClicked(View view) {
        finish();
    }
}