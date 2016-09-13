package com.gut.follower.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gut.follower.utility.SessionManager;

public class SplashActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SessionManager.isUserLoggedIn(getApplicationContext())){
            startNewActivity(MainActivity.class);
        } else {
            startNewActivity(LoginActivity.class);
        }
    }

}
