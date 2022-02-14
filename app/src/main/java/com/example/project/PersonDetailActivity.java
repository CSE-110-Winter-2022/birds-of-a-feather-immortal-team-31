package com.example.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.Course;

import java.io.IOException;
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

        String name = getIntent().getStringExtra("user_name");
        String url = getIntent().getStringExtra("user_photoURL");
        List<Course> courses = (List<Course>) getIntent().getSerializableExtra("user_courses");

        TextView PersonDetailName = findViewById(R.id.PersonDetailName);
        PersonDetailName.setText(name);

        ImageView imageView = findViewById(R.id.imageView);

        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeFile(String.valueOf(new java.net.URL(url).openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bmp);



        coursesRecyclerView = findViewById(R.id.courses_view);

        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        courseViewAdapter = new CourseViewAdapter(courses);
        coursesRecyclerView.setAdapter(courseViewAdapter);
    }

    public void onBackButtonClicked(View view){
        finish();
    }
}