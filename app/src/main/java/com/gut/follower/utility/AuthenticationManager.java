package com.gut.follower.utility;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.gut.follower.activities.MainActivity;
import com.gut.follower.model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gut.follower.commons.InputValidator.checkIfUserCredentialsNotEmpty;
import static com.gut.follower.commons.InputValidator.createAccount;

public class AuthenticationManager {

    private AppCompatActivity callerActivity;
    private JConductorService restApi;
    private String username;
    private String password;
    private String email;

    public AuthenticationManager(AppCompatActivity callerActivity, EditText username, EditText password) {
        this.callerActivity = callerActivity;
        this.restApi = ServiceGenerator.createService(JConductorService.class);
        this.username = username.getText().toString();
        this.password = password.getText().toString();
    }

    public AuthenticationManager(AppCompatActivity callerActivity, EditText username, EditText password, EditText email) {
        this.callerActivity = callerActivity;
        this.restApi = ServiceGenerator.createService(JConductorService.class);
        this.username = username.getText().toString();
        this.password = password.getText().toString();
        this.email = email.getText().toString();
    }

    public void login(){
        if(checkIfUserCredentialsNotEmpty(username, password)){
            Call<Account> call = restApi.login(createAccount(username, password));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(callerActivity.getApplicationContext(), MainActivity.class);
                        callerActivity.startActivity(intent);
                    } else {
                        Toast.makeText(callerActivity.getApplicationContext(),
                                response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toast.makeText(callerActivity.getApplicationContext(),
                            t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(callerActivity.getApplicationContext(),
                    "User credentials can not be empty",
                    Toast.LENGTH_SHORT).show();
    }

    public void register(){
        if(checkIfUserCredentialsNotEmpty(username, password, email)){
            Account account = createAccount(username, password, email);
            Call<Account> call = restApi.register(account);
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        login();
                    } else {
                        Toast.makeText(callerActivity.getApplicationContext(),
                                response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toast.makeText(callerActivity.getApplicationContext(),
                            t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
