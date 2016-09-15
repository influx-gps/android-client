package com.gut.follower.utility;

import android.content.Context;

public class SessionManager {

    public static boolean isUserLoggedIn(Context context){
        UserCredentials userCredentials = new UserCredentials(context);
        if(!userCredentials.getUsername().isEmpty() && !userCredentials.getPassword().isEmpty()){
            return true;
        }
        return false;
    }

    public static void saveUserCredentials(Context context, String username, String password){
        UserCredentials userCredentials = new UserCredentials(context);
        userCredentials.setUsername(username);
        userCredentials.setPassword(password);
    }

    public static void clearUserCredentials(Context context){
        UserCredentials userCredentials = new UserCredentials(context);
        userCredentials.clearPassword();
        userCredentials.clearUsername();
    }
}
