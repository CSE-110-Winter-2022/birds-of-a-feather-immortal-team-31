package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chooseListActivity extends AppCompatActivity {

    HashMap<String, ArrayList<Integer>> userLists;
    ArrayList<Integer> currList;
    public RecyclerView keyRecyclerView;
    public RecyclerView.LayoutManager keyLayoutManager;
    public KeyViewAdapter keyViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);
        Intent intent = getIntent();
        userLists = (HashMap<String, ArrayList<Integer>>) intent.getSerializableExtra("userLists");
        currList = intent.getIntegerArrayListExtra("currList");

        keyRecyclerView = findViewById(R.id.lists_view);
        keyLayoutManager = new LinearLayoutManager(this);
        keyRecyclerView.setLayoutManager(keyLayoutManager);

        keyViewAdapter = new KeyViewAdapter(new ArrayList<String>(userLists.keySet()));
        keyRecyclerView.setAdapter(keyViewAdapter);
    }

    public void onConfirmClicked(View view)
    {
        TextView textView = findViewById(R.id.choice);
        String choice = textView.getText().toString();
        while (!userLists.containsKey(choice))
        {
            Toast.makeText(getApplicationContext(),"Data does not exist",Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent (this, chooseListActivity.class);
            intent1.putIntegerArrayListExtra("currList",currList);
            intent1.putExtra("userLists", userLists);
            startActivity(intent1);
        }
        currList = userLists.get(choice);
        finish();
    }
}