package com.gut.follower.activities.loginactivity;

import com.gut.follower.utility.AuthenticationManager;

public class LoginPresenter implements LoginContract.Presenter{

    private LoginContract.View view;

    @Override
    public void attachView(LoginContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void login(String username, String password) {
        new AuthenticationManager(view.getContext(), username, password).login();
    }
}
