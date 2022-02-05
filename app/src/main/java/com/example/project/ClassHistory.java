package com.example.project;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;

public class ClassHistory extends AppCompatActivity {

    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_history);
    }

    public void onSaveHistoryButtonClicked(View view){
        TextView yearView = findViewById(R.id.editTextYear);
        TextView quarterView = findViewById(R.id.editTextQuarter);
        TextView subjectView = findViewById(R.id.editTextSubject);
        TextView numberView = findViewById(R.id.editTextNumber);

        String subjectAndNumber = subjectView.getText().toString() + numberView.getText().toString();
        String quarter = quarterView.getText().toString();
        int year = Integer.parseInt(yearView.getText().toString());
        Log.d(TAG, "subject: " + subjectAndNumber);
        Log.d(TAG, "quarter: " +quarter);
        Log.d(TAG, "year: " + String.valueOf(year));
        Course newCourse = new Course(year, quarter, subjectAndNumber);
        Log.d(TAG, "courseYear: "+ newCourse.getYear()+ "subject :" + newCourse.getSubjectAndNumber() + "quarter: " + newCourse.getQuarter());


        db.coursesDao().insert(newCourse);
    }

}