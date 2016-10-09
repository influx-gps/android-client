package com.gut.follower.activities.loginactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gut.follower.R;
import com.gut.follower.activities.BaseActivity;
import com.gut.follower.activities.mainactivity.MainActivity;
import com.gut.follower.activities.registeractivity.RegisterActivity;

public class LoginActivity extends BaseActivity implements LoginContract.View{

    private LoginContract.Presenter mPresenter;

    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
    private TextView mRegister;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
    public Context getContext() {
        return this;
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void hideKeyboard() {
        super.hideKeyboard(this);
    }
}
