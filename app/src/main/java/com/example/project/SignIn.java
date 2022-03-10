package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

    }

    public void onButtonSaveUserInfoClicked(View view){
        SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);

        TextView usernameView = findViewById(R.id.editTextName);
        TextView URLView = findViewById(R.id.editTextURL);

        String id = "848985";
        /*
        if(preferences.getString("ID", null) == null) {
            id = String.valueOf((int) (Math.random()*1000000.0));
        }
        else id = preferences.getString("ID", null);

         */
        Log.d("ID", id);

        //myid = 848985

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NAME", usernameView.toString());
        editor.putString("URL", URLView.toString());
        editor.putString("ID", id);
        finish();
    }
}