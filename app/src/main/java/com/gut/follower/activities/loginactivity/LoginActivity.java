package com.gut.follower.activities.loginactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gut.follower.R;
import com.gut.follower.utility.AuthenticationManager;
import com.gut.follower.activities.BaseActivity;
import com.gut.follower.activities.registeractivity.RegisterActivity;

public class LoginActivity extends BaseActivity implements LoginContract.View{

    private LoginContract.Presenter mPresenter;

    private EditText username;
    private EditText password;
    private Button loginButton;
    private TextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.login_btn);
        register = (TextView)findViewById(R.id.register);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.login(username.getText().toString(), password.getText().toString());
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterActivity();
            }
        });
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoadingSpinner() {
        
    }

    @Override
    public void hideUserLoginForm() {

    }
}
