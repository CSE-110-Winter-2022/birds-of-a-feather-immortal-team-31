package com.example.project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;

import java.security.Provider;
import java.util.List;

public class PublishMessageService extends Service {

    public int onStartCommand(Intent intent, int flags, int startId) {
        //setContentView(R.layout.activity_publish_message);

        AppDatabase db = AppDatabase.singleton(getApplicationContext());
        List<Course> myCourses = db.coursesDao().getAll();


        SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String usernameFinal = preferences.getString("NAME", null);
        String photourlFinal = preferences.getString("URL", null);
        String myId = preferences.getString("ID", null);

        String wavedHandsAll = preferences.getString("WavedUsers", null);

        String messageString = "B3%&J";
        messageString += usernameFinal + "," + photourlFinal + "," + myId + ",";

        for (Course c : myCourses) {
            messageString += c.courseToString();
        }
        messageString += ":" + wavedHandsAll;

        Message databaseMessage = new Message(messageString.getBytes());
        Nearby.getMessagesClient(this).publish(databaseMessage);
        Log.d("Message published", "true");
        return startId;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}