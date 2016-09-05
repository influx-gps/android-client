package com.gut.follower.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gut.follower.R;
import com.gut.follower.utility.AuthenticationManager;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;

public class RegisterActivity extends AppCompatActivity{

    private EditText email;
    private EditText username;
    private EditText password;
    private Button registerBtn;

    private JConductorService jConductorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        jConductorService = ServiceGenerator.createService(JConductorService.class);
    }

    private void initView() {
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerBtn = (Button) findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AuthenticationManager(RegisterActivity.this, username, password, email)
                        .register();
            }
        });
    }
}
