package com.example.project;

import static com.example.project.MainActivity.getMainActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.project.model.AppDatabase;
import com.example.project.model.User;

import java.util.ArrayList;
import java.util.List;

public class LoadSessionActivity extends AppCompatActivity {

    private AppDatabase db;
    private List<User> allUsers;

    public RecyclerView keyRecyclerView;
    public RecyclerView.LayoutManager keyLayoutManager;
    public KeyViewAdapter keyViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_session);
        Log.d("in activity", " ");

        SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
        String allSessions = preferences.getString("Sessions", null);

        Log.d("still here", " ");

        //if(allSessions==null) Utilities.showAlert(this, "You have no sessions saved");

        List <String> listSessions = new ArrayList<String>();

        if(allSessions!=null){
            for(int i = 0; i < allSessions.length(); i++){
                int oldI = i;
                while (true && i < allSessions.length()){
                    if(allSessions.charAt(i) == '|'){
                        String thisSession = allSessions.substring(oldI, i);
                        listSessions.add(thisSession);
                        break;
                    }
                    i++;
                }
            }
        }

        Log.d("Still here", " ");



        keyRecyclerView = findViewById(R.id.lists_view);
        keyLayoutManager = new LinearLayoutManager(this);
        keyRecyclerView.setLayoutManager(keyLayoutManager);

        keyViewAdapter = new KeyViewAdapter(listSessions);
        keyRecyclerView.setAdapter(keyViewAdapter);
    }

    public void onConfirmClicked(View view){
        TextView selSes = findViewById(R.id.choice);
        String selectedSession = selSes.getText().toString();

        List <User> usersFromSession = new ArrayList<User>();


        db = AppDatabase.singleton(getApplicationContext());
        allUsers = db.usersDao().getAll();

        for(User u : allUsers){
            if(u.session.equals(selectedSession)){
                usersFromSession.add(u);
            }
        }
        Log.d("Size of usersFromSession", " "+ usersFromSession.size());
        Log.d("name", usersFromSession.get(0).getName());

        getMainActivity().setFellowUsers(usersFromSession);

        finish();}


}