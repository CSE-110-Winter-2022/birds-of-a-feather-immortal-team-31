package com.example.project;

import static com.example.project.MainActivity.getMainActivity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        db = FavoriteUserDatabase.singleton(getApplicationContext());
        favorite_users = db.usersDao().getAll();

        usersRecyclerView = findViewById(R.id.star_view);

        usersLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(usersLayoutManager);

        userViewAdapter = new UsersViewAdapter(getApplicationContext(),favorite_users);

        usersRecyclerView.setAdapter(userViewAdapter);
    }

    //Helper method that set the stars correctly on the main activity
    public void setStars(){
        List<User> fellowUsers = getMainActivity().getFellowUsers();
        for(User fellowUser: fellowUsers){
            for(User favorite_user:this.userViewAdapter.getUsers()){
                //Should've been compared by id, but didn't work
                if(fellowUser.getId() == favorite_user.getId()){
                    fellowUser.setStar(favorite_user.getStar());
                }
            }
        }
        getMainActivity().setFellowUsers(fellowUsers);
        getMainActivity().userViewAdapter = new UsersViewAdapter(getApplicationContext(), fellowUsers);
        getMainActivity().usersRecyclerView.setAdapter(getMainActivity().userViewAdapter);
    }

    public void onBackButtonClicked(View view) {
        setStars();
        finish();
    }
}
