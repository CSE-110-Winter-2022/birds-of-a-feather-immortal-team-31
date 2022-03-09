package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.Course;

import java.util.List;

public class PersonDetailActivity extends AppCompatActivity {
    protected RecyclerView coursesRecyclerView;
    protected RecyclerView.LayoutManager coursesLayoutManager;
    protected CourseViewAdapter courseViewAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);


        String name = getIntent().getStringExtra("user_name");
        String url = getIntent().getStringExtra("user_photoURL");
        List<Course> courses = (List<Course>) getIntent().getSerializableExtra("user_courses");

        TextView PersonDetailName = findViewById(R.id.PersonDetailName);
        PersonDetailName.setText(name);

        ImageView imageView = findViewById(R.id.imageView);

        /*Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeFile(String.valueOf(new java.net.URL(url).openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bmp);*/



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