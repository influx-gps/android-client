package com.gut.follower;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gut.follower.model.Account;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private TextView register;

    private JConductorService jConductorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.login_btn);
        register = (TextView)findViewById(R.id.register);

        jConductorService = ServiceGenerator.createService(JConductorService.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        if(checkIfUserCredentialsNotEmpty()){
            Call<Account> call = jConductorService.register(new Account(username.getText().toString(), password.getText().toString(), "soemmail"));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        loginUser();
                    } else {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loginUser() {
        if(checkIfUserCredentialsNotEmpty()){
            Call<Account> call = jConductorService.login(new Account(username.getText().toString(), password.getText().toString()));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private boolean checkIfUserCredentialsNotEmpty(){
        if("".equals(username.getText().toString()) || "".equals(password.getText().toString())){
            Toast.makeText(getApplicationContext(), "User credentials can not be empty", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
