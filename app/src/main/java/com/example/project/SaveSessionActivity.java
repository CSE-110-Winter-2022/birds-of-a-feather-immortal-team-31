package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.project.model.AppDatabase;
import com.example.project.model.Course;
import com.example.project.model.User;
import com.example.project.model.UsersDao;

import java.util.List;

public class SaveSessionActivity extends AppCompatActivity {

    public List<User> fellowUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_session);

        fellowUsers = MainActivity.getMainActivity().getFellowUsers();
    }

    public void onSaveSessionButtonClicked(View view){
        TextView sessionNameTV = (TextView) findViewById(R.id.nameSaveSession);
        String sessionName = sessionNameTV.getText().toString();
        SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String allSessions = preferences.getString("Sessions", null);
        SharedPreferences.Editor editor = preferences.edit();

        AppDatabase db = AppDatabase.singleton(getApplicationContext());

        boolean alreadyEntered = false;

        if(allSessions!=null) Log.d("all sessions", allSessions);
        else allSessions = "";

        if(allSessions!=null){
            for(int i = 0; i < allSessions.length(); i++){
                int oldI = i;
                while (true && i < allSessions.length()){
                    if(allSessions.charAt(i) == '|'){
                        String thisSession = allSessions.substring(oldI, i);
                        if(thisSession.equals(sessionName)){
                            Utilities.showAlert(this, "A session with this name already exists!");
                            alreadyEntered = true;
                            break;
                        }
                    }
                    i++;
                }
            }
        }

        if(!alreadyEntered){
            editor.putString("Sessions", allSessions += sessionName + "|");
            editor.apply();
            for(User u : fellowUsers){
                u.setSession(sessionName);
                db.usersDao().insert(u);
            }
        }

        finish();

    }
    public void onBackButtonClickedSS(View view){finish();}

}