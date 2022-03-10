package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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


    Boolean alreadyWaved = false;
    Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);


        String name = getIntent().getStringExtra("user_name");
        String url = getIntent().getStringExtra("user_photoURL");
        String id = getIntent().getStringExtra("user_id");
        Log.e("url", url);
        List<Course> courses = (List<Course>) getIntent().getSerializableExtra("user_courses");

        SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String wavedUsers = preferences.getString("WavedUsers", null);

        Button wave = (Button) findViewById(R.id.buttonWave);
        if(wavedUsers!=null) {
            for (int i = 0; i < wavedUsers.length(); i += 7) {
                if (wavedUsers.substring(i, i + 6).equals(id)) {
                    Log.d("WavedID", wavedUsers.substring(i, i + 6));
                    wave.setBackgroundColor(Color.BLUE);
                    alreadyWaved = true;
                }
            }
        }

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

    public void onWaveButtonClicked(View view){
        Button wave = (Button) findViewById(R.id.buttonWave);
        if(!alreadyWaved) {
            wave.setBackgroundColor(Color.BLUE);
            SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
            String wavedUsers = preferences.getString("WavedUsers", null);
            String id = getIntent().getStringExtra("user_id");
            if(wavedUsers == null) wavedUsers = id + ",";
            else wavedUsers += id + ",";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("WavedUsers", wavedUsers);
            editor.apply();
            Log.d("waved to", preferences.getString("WavedUsers", null));


            Intent intent = new Intent(this, PublishMessageService.class);
            startService(intent);
        }

    }
}