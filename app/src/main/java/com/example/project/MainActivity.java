package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MessageListener messageListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String username = preferences.getString("NAME", null);
        String photourl = preferences.getString("URL", null);

        if(username == null && photourl == null){
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
        }

        /*
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);


        if(account == null){
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            Intent switchActivites = new Intent(this, SignIn.class);

        }

         */


        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        List<Course> myCourses = db.coursesDao().getAll();

        String usernameFinal = preferences.getString("NAME", null);
        String photourlFinal = preferences.getString("URL", null);

        String messageString = "B3%&J";
        messageString += usernameFinal + "," + photourlFinal + ",";
        for (Course c : myCourses) {
            messageString += c.courseToString();
        }

        String FakedMessageString = "B3%&J" + "Bjarki," + "demoURL,";

        List<Course> demoCourse = new ArrayList<Course>();

        Course demo1 = new Course(2020, "Fall", "CSE110");
        Course demo2 = new Course(2021, "Spring", "CSE101");
        Course demo3 = new Course(2021, "Fall", "CSE2");

        demoCourse.add(demo1);
        demoCourse.add(demo2);
        demoCourse.add(demo3);

        for (Course c : demoCourse) {
            FakedMessageString += c.courseToString();
        }

        List<FellowStudent> fellowStudents = new ArrayList<FellowStudent>();


        Message databaseMessage = new Message(messageString.getBytes());
        Nearby.getMessagesClient(this).publish(databaseMessage);

        MessageListener messageListener = new MessageListener() {
            @Override
            public void onFound(@NonNull Message message) {
                String sMessage = new String(message.getContent());
                Log.d("MEssage found", sMessage.substring(0,5));
                if (sMessage.substring(0, 5).equals("B3%&J")) { //authentication key of message

                    int k1 = 5;
                    while (true) {
                        k1++;
                        if (sMessage.charAt(k1) == ',') break;

                    }
                    String fellowStudentName = sMessage.substring(5, k1);

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

        this.messageListener = new FakedMessageListener(messageListener, 60, FakedMessageString);
    }

    public void onButtonOpenHistoryClicked(View view){
        Intent intent = new Intent(this, ClassHistory.class);
        startActivity(intent);
    }

    public void onViewMutualCoursesClicked(View view){
        Intent intent = new Intent(this, PersonDetailActivity.class);
        startActivity(intent);
    }

    private boolean isUserSignedIn;


}