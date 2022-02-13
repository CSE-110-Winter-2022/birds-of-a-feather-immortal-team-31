package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        List<Course> courses = db.coursesDao().getAll();
    }

    public void onButtonOpenHistoryClicked(View view){
        Intent intent = new Intent(this, ClassHistory.class);
        startActivity(intent);
    }

    public void onViewMutualCoursesClicked(View view){
        Intent intent = new Intent(this, PersonDetailActivity.class);
        startActivity(intent);
    }

    public void onSearchForClassmatesClicked(View view) {
        Intent intent = new Intent(this, SearchForClassmates.class);
        startActivity(intent);
    }
}