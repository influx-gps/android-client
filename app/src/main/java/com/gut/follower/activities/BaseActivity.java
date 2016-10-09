package com.gut.follower.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gut.follower.activities.loginactivity.LoginActivity;
import com.gut.follower.activities.mainactivity.MainActivity;
import com.gut.follower.utility.SessionManager;

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this instanceof LoginActivity){
            if(SessionManager.isUserLoggedIn(getApplicationContext())){
                startNewActivity(MainActivity.class);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void startNewActivity(Class<? extends AppCompatActivity> activity){
        Intent intent = new Intent(getApplicationContext(), activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
