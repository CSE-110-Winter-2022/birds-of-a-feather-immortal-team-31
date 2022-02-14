package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.example.project.model.User;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<User> fellowUsers = new ArrayList<User>();

    protected RecyclerView usersRecyclerView;
    protected RecyclerView.LayoutManager usersLayoutManager;
    protected UsersViewAdapter userViewAdapter;


    protected List<Course> courses = new ArrayList<Course>();
    protected Course demo1 = new Course(2077, "Spring", "CSE110");
    protected Course demo2 = new Course(2019, "Winter", "CSE101");

    protected User user1 = new User("Luffy","ssdssd",courses);
    protected User user2 = new User("Zoro","fdfdf",courses);

    public MessageListener messageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        courses.add(demo1);
        courses.add(demo2);
        fellowUsers.add(user1);
        fellowUsers.add(user2);

        usersRecyclerView = findViewById(R.id.users_view);

        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        userViewAdapter = new UsersViewAdapter(fellowUsers);
        usersRecyclerView.setAdapter(userViewAdapter);

        usersRecyclerView.setVisibility(View.INVISIBLE);


        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        List<Course> myCourses = db.coursesDao().getAll();

        String messageString = "B3%&J";

        //needs string for user's name and user google photo url

        for (Course c : myCourses) {
            messageString += c.courseToString() + ",";
        }



        Message databaseMessage = new Message(messageString.getBytes());
        Nearby.getMessagesClient(this).publish(databaseMessage);

        this.messageListener = new MessageListener() {
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
                        User user = new User(fellowStudentName, fellowStudentPhotoURL, fellowStudentMutualCourse);
                        fellowUsers.add(user);
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

    public void onSearchForClassmatesClicked(View view){

        TextView textView = findViewById(R.id.Search_for_classmates);
        String text = textView.getText().toString();

        if(text.equals("Start")){
            usersRecyclerView.setVisibility(View.VISIBLE);
            textView.setText("Stop");
        }
        else if (text.equals("Stop")){
            Nearby.getMessagesClient(this).unsubscribe(this.messageListener);
            textView.setText("Start");
        }
    }
}