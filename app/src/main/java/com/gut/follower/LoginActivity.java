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

import static com.gut.follower.commons.InputValidator.checkIfUserCredentialsNotEmpty;
import static com.gut.follower.commons.InputValidator.createAccount;

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
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void loginUser() {
        // TODO: This method is used in two Activities - we need to find out how to separate it.
        if(checkIfUserCredentialsNotEmpty(username, password)){
            Call<Account> call = jConductorService.login(createAccount(username, password));
            call.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                       response.message(),
                                       Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),
                                   t.getMessage(),
                                   Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(),
                           "User credentials can not be empty",
                           Toast.LENGTH_SHORT).show();
    }
}
