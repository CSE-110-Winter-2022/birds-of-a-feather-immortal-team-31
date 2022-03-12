package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private int STORAGE_PERMISSION_CODE=1;
    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    //TextView textView;
    private static final int RC_SIGN_IN = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

    }

    public void onButtonSaveUserInfoClicked(View view){
        SharedPreferences preferences = getSharedPreferences("USERINFO", Context.MODE_PRIVATE);

        TextView usernameView = findViewById(R.id.editTextName);
        TextView URLView = findViewById(R.id.editTextURL);

        String id;

        if(preferences.getString("ID", null) == null) {
            id = String.valueOf((int) (Math.random()*1000000.0));
        }
        else id = preferences.getString("ID", null);

        String username = usernameView.getText().toString();
        String url = URLView.getText().toString();

        Log.d("ID", id);

        //myid = 848985

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("NAME", username);
        editor.putString("URL", url);
        editor.putString("ID", id);
        editor.apply();
        finish();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            gotoProfile();
        } else {
            Toast.makeText(getApplicationContext(), "Sign in cancel", Toast.LENGTH_LONG).show();
        }
    }
    private void gotoProfile() {
        Intent intent = new Intent(SignIn.this, ProfileActivity.class);
        startActivity(intent);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}