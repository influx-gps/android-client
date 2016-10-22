package com.gut.follower.utility;

public class SessionManager {

    public static boolean isUserLoggedIn(){
        UserCredentials userCredentials = new UserCredentials();
        if(!userCredentials.getUsername().isEmpty() && !userCredentials.getPassword().isEmpty()){
            return true;
        }
        return false;
    }

    public static void saveUserCredentials(String username, String password){
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(username);
        userCredentials.setPassword(password);
    }

    public static void clearUserCredentials(){
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.clearPassword();
        userCredentials.clearUsername();
    }

    public static String getUsername(){
        return new UserCredentials().getUsername();
    }

    public static String getPassword(){
        return new UserCredentials().getPassword();
    }
}
