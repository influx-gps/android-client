package com.gut.follower.utility;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.gut.follower.activities.loginactivity.LoginActivity;
import com.gut.follower.activities.mainactivity.MainActivity;
import com.gut.follower.model.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gut.follower.commons.InputValidator.checkIfUserCredentialsNotEmpty;
import static com.gut.follower.commons.InputValidator.createAccount;

public class AuthenticationManager {

    private Context context;
    private JConductorService restApi;
    private String username;
    private String password;
    private String email;


    public AuthenticationManager(Context context) {
        this.context = context;
    }

    public AuthenticationManager(Context context, String username, String password) {
        this.context = context;
        this.restApi = ServiceGenerator.createService(JConductorService.class);
        this.username = username;
        this.password = password;
    }

    public AuthenticationManager(Context context, String username, String password, String email) {
        this.context = context;
        this.restApi = ServiceGenerator.createService(JConductorService.class);
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void login(){
        if(checkIfUserCredentialsNotEmpty(username, password)){
            Call<Account> call = restApi.login(createAccount(username, password));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        SessionManager.saveUserCredentials(context.getApplicationContext(), username, password);
                        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context.getApplicationContext(),
                                response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toast.makeText(context.getApplicationContext(),
                            t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(context.getApplicationContext(),
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
                        Toast.makeText(context.getApplicationContext(),
                                response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toast.makeText(context.getApplicationContext(),
                            t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void logout(){
        SessionManager.clearUserCredentials(context.getApplicationContext());
        Intent intent = new Intent(context.getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

}
