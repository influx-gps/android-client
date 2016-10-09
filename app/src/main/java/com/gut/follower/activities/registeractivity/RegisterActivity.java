package com.gut.follower.activities.registeractivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gut.follower.R;
import com.gut.follower.activities.BaseActivity;

public class RegisterActivity extends BaseActivity implements RegisterContract.View{

    private RegisterContract.Presenter mPresenter;

    private EditText mEmail;
    private EditText mUsername;
    private EditText mPassword;
    private Button mRegisterBtn;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        mPresenter = new RegisterPresenter();
        mPresenter.attachView(this);
        mProgressBar = (ProgressBar)findViewById(R.id.register_loading);
        mEmail = (EditText) findViewById(R.id.email);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.register(mUsername.getText().toString(),
                        mPassword.getText().toString(),
                        mEmail.getText().toString());
            }
        });
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
    public void hideUserRegisterForm() {
        mUsername.setVisibility(View.GONE);
        mPassword.setVisibility(View.GONE);
        mEmail.setVisibility(View.GONE);
        mRegisterBtn.setVisibility(View.GONE);
    }

    @Override
    public void showUserRegisterForm() {
        mUsername.setVisibility(View.VISIBLE);
        mPassword.setVisibility(View.VISIBLE);
        mEmail.setVisibility(View.VISIBLE);
        mRegisterBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideKeyboard() {
        super.hideKeyboard(this);
    }
}
