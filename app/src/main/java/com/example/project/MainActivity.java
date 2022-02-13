package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        List<Course> myCourses = db.coursesDao().getAll();

        String messageString = "B3%&J";

        //needs string for user's name and user google photo url

        for (Course c : myCourses) {
            messageString += c.courseToString() + ",";
        }

        List<FellowStudent> fellowStudents = new ArrayList<FellowStudent>();


        Message databaseMessage = new Message(messageString.getBytes());
        Nearby.getMessagesClient(this).publish(databaseMessage);

        MessageListener messageListener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                String sMessage = new String(message.getContent());
                if (sMessage.substring(0, 4).equals("B3%&J")) { //authentication key of message
                    int k1 = 4;
                    while (true) {
                        k1++;
                        if (sMessage.charAt(k1) == ',') break;
                    }
                    String fellowStudentName = sMessage.substring(4, k1);

                    int k2 = k1;
                    while (true) {
                        k2++;
                        if (sMessage.charAt(k2) == ',') break;
                    }
                    String fellowStudentPhotoURL = sMessage.substring(k1, k2);

                    List<Course> fellowStudentMutualCourse = new ArrayList<Course>();

                    for (int i = k2 + 1; i < sMessage.length(); i++) {
                        int oldI = i;
                        String fellowStudentSubjectAndNumber = "";
                        String fellowStudentQuarter = "";
                        String fellowStudentYear = "";
                        Course fellowStudentThisCourse;

                        while (true) {
                            if (sMessage.charAt(i) == '%') {//subject and course divider
                                fellowStudentSubjectAndNumber = sMessage.substring(oldI, i);
                                oldI = i + 1;
                                i++;
                                continue;
                            } else if (sMessage.charAt(i) == '^') {//Quarter divider
                                fellowStudentQuarter = sMessage.substring(oldI, i);
                                oldI = i + 1;
                                i++;
                                continue;
                            } else if (sMessage.charAt(i) == '~') {//year and course divider
                                fellowStudentYear = sMessage.substring(oldI, i);
                                fellowStudentThisCourse = new Course(Integer.parseInt(fellowStudentYear),
                                        fellowStudentQuarter, fellowStudentSubjectAndNumber);
                                for (Course myCourse : myCourses) {
                                    if (myCourse.equals(fellowStudentThisCourse)) {
                                        fellowStudentMutualCourse.add(fellowStudentThisCourse);
                                    }
                                }
                                break;
                            } else i++;
                        }
                    }

                    if (!fellowStudentMutualCourse.isEmpty()) {
                        FellowStudent fellowStudent = new FellowStudent(fellowStudentName, fellowStudentPhotoURL, fellowStudentMutualCourse);
                        fellowStudents.add(fellowStudent);
                    }
                }
            }
        };

        Nearby.getMessagesClient(this).subscribe(messageListener);
    }

    public void onButtonOpenHistoryClicked(View view){
        Intent intent = new Intent(this, ClassHistory.class);
        startActivity(intent);
    }

    public void onViewMutualCoursesClicked(View view){
        Intent intent = new Intent(this, PersonDetailActivity.class);
        startActivity(intent);
    }
}