package com.gut.follower.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.gut.follower.activities.login.LoginActivity;
import com.gut.follower.activities.main.MainActivity;
import com.gut.follower.utility.SessionManager;

public class BaseActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this instanceof LoginActivity){
            if(SessionManager.isUserLoggedIn()){
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

    protected void hideKeyboard(AppCompatActivity activity){
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        view.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
