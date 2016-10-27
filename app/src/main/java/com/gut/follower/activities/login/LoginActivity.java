package com.gut.follower.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gut.follower.R;
import com.gut.follower.activities.BaseActivity;
import com.gut.follower.activities.main.MainActivity;
import com.gut.follower.activities.register.RegisterActivity;

public class LoginActivity extends BaseActivity implements LoginContract.View{

    private LoginContract.Presenter mPresenter;

    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
    private TextView mRegister;
    private ProgressBar mProgressBar;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);

        mUsername = (EditText)findViewById(R.id.username);
        mPassword = (EditText)findViewById(R.id.password);
        mLoginButton = (Button)findViewById(R.id.login_btn);
        mRegister = (TextView)findViewById(R.id.register);
        mProgressBar = (ProgressBar)findViewById(R.id.login_loading);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.login(mUsername.getText().toString(), mPassword.getText().toString());
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
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
    public void showLoadingSpinner() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingSpinner() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideUserLoginForm() {
        mLoginButton.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);
        mUsername.setVisibility(View.GONE);
        mRegister.setVisibility(View.GONE);
    }

    @Override
    public void showUserLoginForm() {
        mLoginButton.setVisibility(View.VISIBLE);
        mPassword.setVisibility(View.VISIBLE);
        mUsername.setVisibility(View.VISIBLE);
        mRegister.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void hideKeyboard() {
        super.hideKeyboard(this);
    }
}
