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
        db = AppDatabase.singleton(this);
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

        Course newCourse = new Course(year, quarter, subjectAndNumber);

        db.coursesDao().insert(newCourse);
    }
    public void saveProfile(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        TextView yearView = findViewById(R.id.editTextYear);
        TextView quarterView = findViewById(R.id.editTextQuarter);
        TextView subjectView = findViewById(R.id.editTextSubject);
        TextView numberView = findViewById(R.id.editTextNumber);
        editor.putString("year",yearView.getText().toString());
        editor.putString("quarter",quarterView.getText().toString());
        editor.putString("subject",subjectView.getText().toString());
        editor.putString("number",numberView.getText().toString());
        editor.apply();
    }
}