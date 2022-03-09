package com.example.project;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;

import com.example.project.model.FavoriteUserDatabase;
import com.example.project.model.User;

import java.util.List;

public class FavoriteStudentActivity extends AppCompatActivity
{

    public RecyclerView usersRecyclerView;
    public RecyclerView.LayoutManager usersLayoutManager;
    public UsersViewAdapter userViewAdapter;
    private FavoriteUserDatabase db;
    private List<User> favorite_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_students);

        db = FavoriteUserDatabase.singleton(ApplicationProvider.getApplicationContext());
        favorite_users = db.usersDao().getAll();

        usersRecyclerView = findViewById(R.id.star_view);

        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        userViewAdapter = new UsersViewAdapter(favorite_users);

        usersRecyclerView.setAdapter(userViewAdapter);

    }

    public void onBackButtonClicked(View view) {
        finish();
    }
}
