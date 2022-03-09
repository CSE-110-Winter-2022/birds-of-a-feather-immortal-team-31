package com.example.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import java.util.concurrent.Executor;

public class PersonDetailActivity extends AppCompatActivity {
    protected RecyclerView coursesRecyclerView;
    protected RecyclerView.LayoutManager coursesLayoutManager;
    protected CourseViewAdapter courseViewAdapter;

    protected List<Course> data = new ArrayList<Course>();
    protected Course demo1 = new Course(2018, "Fall", "CSE110", "tiny");
    protected Course demo2 = new Course(2019, "Winter", "CSE101", "medium");

    Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        data.add(demo1);
        data.add(demo2);

        String name = getIntent().getStringExtra("user_name");
        String url = getIntent().getStringExtra("user_photoURL");
        Log.e("url", url);
        List<Course> courses = (List<Course>) getIntent().getSerializableExtra("user_courses");

        TextView PersonDetailName = findViewById(R.id.PersonDetailName);
        PersonDetailName.setText(name);

        ImageView imageView = findViewById(R.id.imageViewPersonDetail);
        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);

        //Bitmap bmp=null;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bmp = BitmapFactory.decodeStream(new java.net.URL("https://photos.google.com/share/AF1QipPXZ1OjS5OrnEiMwQM148V32RLUGJqHOCx2JcgWPCI5wPGDSafHQ6iHBJg3eXkbzA/photo/AF1QipOvyH2ciDKs-mt0FlMm9xLZjqIjrGzTqCcunhbg?key=NDNXZ1RGd3hKb1F6TER4YlJnVDAwSkV1TzlhMDFR").openStream());
                    imageView.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("error", "in bitmapping");
                }
            }
        });
        imageView.setVisibility(View.VISIBLE);







        coursesRecyclerView = findViewById(R.id.courses_view);

        coursesLayoutManager = new LinearLayoutManager(this);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        courseViewAdapter = new CourseViewAdapter(courses);
        coursesRecyclerView.setAdapter(courseViewAdapter);
    }

    public void makeDownloadRequest(Bitmap bmp){

    }

    public void onBackButtonClicked(View view){
        finish();
    }
}