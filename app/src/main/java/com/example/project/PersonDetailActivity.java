package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.example.project.model.Person;

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

        AppDatabase passDb = AppDatabase.singleton(this);

        List<Course> passCourse = passDb.coursesDao().getAll();
        Person student = passDb.personDao().get();

        data.add(demo1);
        data.add(demo2);

        TextView textView = findViewById(R.id.PersonDetailName);
        textView.setText(student.getPersonName());

        coursesRecyclerView = findViewById(R.id.courses_view);

        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        courseViewAdapter = new CourseViewAdapter(passCourse);
        coursesRecyclerView.setAdapter(courseViewAdapter);
    }

    public void onBackButtonClicked(View view){
        finish();
    }
}