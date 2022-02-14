package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.Course;

import java.util.ArrayList;
import java.util.List;

public class PersonDetailActivity extends AppCompatActivity {
    protected RecyclerView coursesRecyclerView;
    protected RecyclerView.LayoutManager coursesLayoutManager;
    protected CourseViewAdapter courseViewAdapter;

    protected List<Course> data = new ArrayList<Course>();
    protected Course demo1 = new Course(2018, "Fall", "CSE110");
    protected Course demo2 = new Course(2019, "Winter", "CSE101");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        data.add(demo1);
        data.add(demo2);

        Intent intent = new Intent(this, PersonDetailActivity.class);
        String name = intent.getStringExtra("user_name");
        String url = intent.getStringExtra("user_photoURL");
        //Course[] courses = (Course[]) intent.getParcelableArrayExtra("user_courses");


        coursesRecyclerView = findViewById(R.id.courses_view);

        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        courseViewAdapter = new CourseViewAdapter(data);
        coursesRecyclerView.setAdapter(courseViewAdapter);
    }

    public void onBackButtonClicked(View view){
        finish();
    }
}