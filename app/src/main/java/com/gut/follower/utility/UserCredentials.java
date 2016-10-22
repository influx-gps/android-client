package com.gut.follower.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.gut.follower.GutFollower;

public class UserCredentials {

    private static final String PREF_NAME = "GutFollower";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private int PRIVATE_MODE = 0;

    private SharedPreferences sharedPreferences;

    public UserCredentials() {
        this.sharedPreferences = GutFollower.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public void setUsername(String username){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public void setPassword(String password){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public String getUsername(){
        return sharedPreferences.getString(USERNAME, "");
    }

    public String getPassword(){
        return sharedPreferences.getString(PASSWORD, "");
    }

    public void clearUsername(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USERNAME);
        editor.commit();
    }

    public void clearPassword(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(PASSWORD);
        editor.commit();
    }
}
