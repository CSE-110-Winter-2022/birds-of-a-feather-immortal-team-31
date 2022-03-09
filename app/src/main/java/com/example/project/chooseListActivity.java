package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chooseListActivity extends AppCompatActivity {

    HashMap<String, ArrayList<User>> userLists;
    ArrayList<User> currList;
    public RecyclerView keyRecyclerView;
    public RecyclerView.LayoutManager keyLayoutManager;
    public KeyViewAdapter keyViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);
        Intent intent = getIntent();
        userLists = intent.getParcelableExtra("userLists");
        Bundle temp = intent.getBundleExtra("temp");
        currList = (ArrayList<User>) temp.getSerializable("currList");
        keyViewAdapter = new KeyViewAdapter((List<String>) userLists.keySet());
        keyRecyclerView.setAdapter(keyViewAdapter);
        TextView textView = findViewById(R.id.choice);

        String choice = textView.getText().toString();
        while (!userLists.containsKey(choice))
        {
            Toast.makeText(getApplicationContext(),"Data does not exist",Toast.LENGTH_LONG).show();
            choice = textView.getText().toString();
        }
        currList = userLists.get(choice);
        finish();
    }
}