package com.example.project;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.nearby.Nearby.getMessagesClient;

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

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.PublishCallback;
import com.google.android.gms.nearby.messages.PublishOptions;
import com.google.android.gms.nearby.messages.Strategy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public Boolean alreadyWaved = false;
    Executor executor;

    private static final int TTL_IN_SECONDS = 20; // Three minutes.

    private static final Strategy PUB_SUB_STRATEGY = new Strategy.Builder()
            .setTtlSeconds(TTL_IN_SECONDS).build();

    public String messageString;
    Message databaseMessage;

    public String name;
    public String url;
    public String id;
    public String wavedUsers;

    public List<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        data.add(demo1);
        data.add(demo2);

        name = getIntent().getStringExtra("user_name");
        url = getIntent().getStringExtra("user_photoURL");
        id = getIntent().getStringExtra("user_id");
        Log.e("url", url);
        courses = (List<Course>) getIntent().getSerializableExtra("user_courses");

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
            wavedUsers = preferences.getString("WavedUsers", null);
            String id = getIntent().getStringExtra("user_id");
            if(wavedUsers == null) wavedUsers = id + ",";
            else wavedUsers += id + ",";
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("WavedUsers", wavedUsers);
            editor.apply();
            Log.d("waved to", preferences.getString("WavedUsers", null));

            alreadyWaved = true;

            String usernameFinal = preferences.getString("NAME", null);
            String photourlFinal = preferences.getString("URL", null);
            String myId = preferences.getString("ID", null);
            myId = "848985";

            String wavedHandsAll = preferences.getString("WavedUsers", null);

            messageString = "B3%&J";
            messageString += usernameFinal + "," + photourlFinal + "," + myId + ",";

            AppDatabase db = AppDatabase.singleton(getApplicationContext());
            List <Course > myCourses = db.coursesDao().getAll();

            for (Course c : myCourses) {
                messageString += c.courseToString();
            }
            messageString += ":" + wavedHandsAll;

            databaseMessage = new Message(messageString.getBytes());
            publish();
        }

    }

    private void publish() {
        Log.i(TAG, "Publishing");
        PublishOptions options = new PublishOptions.Builder()
                .setStrategy(PUB_SUB_STRATEGY)
                .setCallback(new PublishCallback() {
                    @Override
                    public void onExpired() {
                        super.onExpired();
                        Log.i(TAG, "No longer publishing");
                    }
                }).build();


        getMessagesClient(this).publish(databaseMessage, options)
                .addOnFailureListener(e -> {
                    Log.e("Fail", ":(");
                });
        Log.d("database message from person detail Activity" , new String(databaseMessage.getContent()));
    }
}