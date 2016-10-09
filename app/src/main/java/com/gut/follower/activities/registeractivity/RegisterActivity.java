package com.gut.follower.activities.registeractivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gut.follower.R;
import com.gut.follower.utility.AuthenticationManager;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;
import com.gut.follower.activities.BaseActivity;

public class RegisterActivity extends BaseActivity implements RegisterContract.View{

    private RegisterContract.Presenter mPresenter;

    private EditText email;
    private EditText username;
    private EditText password;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        mPresenter = new RegisterPresenter();
        mPresenter.attachView(this);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerBtn = (Button) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.register(username.getText().toString(),
                        password.getText().toString(),
                        email.getText().toString());
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }
}
