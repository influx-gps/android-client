package com.gut.follower.commons;


import android.widget.EditText;

import com.gut.follower.model.Account;

public class InputValidator {

    public static boolean checkIfUserCredentialsNotEmpty(EditText username, EditText password,
                                                         EditText email){
        return !isEmpty(username) && !isEmpty(password) && !isEmpty(email);
    }

    public static boolean checkIfUserCredentialsNotEmpty(EditText username, EditText password){
        return !isEmpty(username) && !isEmpty(password);
    }

    private static boolean isEmpty(EditText field){
        return "".equals(field.getText().toString());
    }

    public static Account createAccount(EditText username, EditText password){
        return new Account(username.getText().toString(), password.getText().toString());
    }

    public static Account createAccount(EditText username, EditText password, EditText email){
        return new Account(username.getText().toString(),
                           password.getText().toString(),
                           email.getText().toString());
    }
}
