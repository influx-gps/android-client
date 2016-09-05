package com.gut.follower.commons;


import android.widget.EditText;

import com.gut.follower.model.Account;

public class InputValidator {

    public static boolean checkIfUserCredentialsNotEmpty(String username, String password,
                                                         String email){
        return !isEmpty(username) && !isEmpty(password) && !isEmpty(email);
    }

    public static boolean checkIfUserCredentialsNotEmpty(String username, String password){
        return !isEmpty(username) && !isEmpty(password);
    }

    private static boolean isEmpty(String field){
        return "".equals(field);
    }

    public static Account createAccount(String username, String password){
        return new Account(username, password);
    }

    public static Account createAccount(String username, String password, String email){
        return new Account(username, password, email);
    }
}
