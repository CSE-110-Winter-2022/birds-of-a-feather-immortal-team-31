package com.example.project;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;

import java.util.List;

public class ClassHistory extends AppCompatActivity {


    private AppDatabase db;
    private AppDatabase passdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = AppDatabase.singleton(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_history);
    }

    public void onSaveHistoryButtonClicked(View view) throws NumberFormatException{
        try {
            TextView yearView = findViewById(R.id.editTextYear);
            TextView quarterView = findViewById(R.id.editTextQuarter);
            TextView subjectView = findViewById(R.id.editTextSubject);
            TextView numberView = findViewById(R.id.editTextNumber);

            String subjectAndNumber = subjectView.getText().toString() + numberView.getText().toString();
            String quarter = quarterView.getText().toString();
            int year = Integer.parseInt(yearView.getText().toString());

            Course newCourse = new Course(year, quarter, subjectAndNumber);
            try{
                db.coursesDao().insert(newCourse);
            }
            catch (SQLiteConstraintException e){
                Utilities.showAlert(this,"Class has already been entered");
            }


            yearView.setText("");
            quarterView.setText("");
            subjectView.setText("");
            numberView.setText("");
        }
        catch(NumberFormatException e){
            Utilities.showAlert(this, "Year has to be a number");
        }


    }

    public void onExitButtonClicked(View view){
        finish();
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

    public void onSearchButtonClicked(View view){
        List<Course> courses = db.coursesDao().getAll();
        List<Course> passCourses = passdb.coursesDao().getAll();
        String studentName = null;

        for (Course i : courses)
        {
            for(Course j : passCourses)
            {
                if (i.equals(j))
                {
                    /*TextView textView = findViewById(R.id.PersonDetailName);
                    textView.setText(studentName);*/
                    startActivity(new Intent(this, PersonDetailActivity.class));
                }
            }
        }

    }
}