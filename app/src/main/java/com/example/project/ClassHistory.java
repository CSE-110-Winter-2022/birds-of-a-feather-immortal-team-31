package com.example.project;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;

import java.util.Locale;

public class ClassHistory extends AppCompatActivity {

    private AppDatabase db;


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
            TextView sizeView = findViewById(R.id.editTextSize);

            String subjectAndNumber = subjectView.getText().toString().toLowerCase(Locale.ROOT) + numberView.getText().toString();
            String quarter = quarterView.getText().toString().toLowerCase(Locale.ROOT);
            int year = Integer.parseInt(yearView.getText().toString());
            String size = sizeView.getText().toString().toLowerCase(Locale.ROOT);

            Course newCourse = new Course(year, quarter, subjectAndNumber, size);
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
            sizeView.setText("");
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
        TextView sizeView = findViewById(R.id.editTextSize);
        editor.putString("year",yearView.getText().toString());
        editor.putString("quarter",quarterView.getText().toString());
        editor.putString("subject",subjectView.getText().toString());
        editor.putString("number",numberView.getText().toString());
        editor.putString("size",sizeView.getText().toString());
        editor.apply();
    }
}