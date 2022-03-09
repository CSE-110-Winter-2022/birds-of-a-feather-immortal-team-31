package com.example.project;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.util.Util;
import com.example.project.model.AppDatabase;
import com.example.project.model.Course;

import java.util.Collections;
import java.util.Locale;

public class ClassHistory extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = AppDatabase.singleton(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_history);

        Spinner quarterSpinner = findViewById(R.id.spinnerQuarter);
        ArrayAdapter<CharSequence>quarterAdapter = ArrayAdapter.createFromResource(this, R.array.Quarters, android.R.layout.simple_spinner_item);
        quarterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quarterSpinner.setAdapter(quarterAdapter);
        quarterSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        Spinner sizeSpinner = findViewById(R.id.spinnerSizes);
        ArrayAdapter<CharSequence>sizesAdapter = ArrayAdapter.createFromResource(this, R.array.ClassSizes, android.R.layout.simple_spinner_item);
        sizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizesAdapter);
        sizeSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

    }

    String qSpinnerString;
    String sSpinnerString;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        // Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        Log.e("item selected", text);
        switch (text)
        {
            case "Fall":
                qSpinnerString = "Fall";
                break;
            case "Winter":
                qSpinnerString = "Winter";
                break;
            case "Spring":
                qSpinnerString = "Spring";
                break;
            case "Summer":
                qSpinnerString = "Summer";
                break;
            case "Tiny":
                sSpinnerString = "Tiny";
                break;
            case "Small":
                sSpinnerString = "Small";
                break;
            case "Medium":
                sSpinnerString = "Medium";
                break;
            case "Large":
                sSpinnerString = "Large";
                break;
            case "Huge":
                sSpinnerString = "Huge";
                break;
            case "Gigantic":
                sSpinnerString = "Gigantic";
                break;


            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onSaveHistoryButtonClicked(View view) throws NumberFormatException{
        try {
            TextView yearView = findViewById(R.id.editTextYear);
            TextView subjectView = findViewById(R.id.editTextSubject);
            TextView numberView = findViewById(R.id.editTextNumber);


            String subjectAndNumber = subjectView.getText().toString().toLowerCase(Locale.ROOT) + numberView.getText().toString();
            String quarter = qSpinnerString.toLowerCase(Locale.ROOT);
            Log.e("Selected", quarter);
            int year = Integer.parseInt(yearView.getText().toString());
            String size = sSpinnerString.toLowerCase(Locale.ROOT);
            Log.e("selected", size);

            Course newCourse = new Course(year, quarter, subjectAndNumber, size);
            try{
                db.coursesDao().insert(newCourse);
                Utilities.showAlert(this, "Course saved");
            }
            catch (SQLiteConstraintException e){
                Utilities.showAlert(this,"Class has already been entered");
            }
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
        //TextView quarterView = findViewById(R.id.editTextQuarter);
        TextView subjectView = findViewById(R.id.editTextSubject);
        TextView numberView = findViewById(R.id.editTextNumber);
        //TextView sizeView = findViewById(R.id.editTextSize);
        editor.putString("year",yearView.getText().toString());
        //editor.putString("quarter",quarterView.getText().toString());
        editor.putString("subject",subjectView.getText().toString());
        editor.putString("number",numberView.getText().toString());
        //editor.putString("size",sizeView.getText().toString());
        editor.apply();
    }
}