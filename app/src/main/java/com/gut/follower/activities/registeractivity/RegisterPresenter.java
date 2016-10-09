package com.gut.follower.activities.registeractivity;

import com.gut.follower.utility.AuthenticationManager;

public class RegisterPresenter implements RegisterContract.Presenter{

    private RegisterContract.View view;

    @Override
    public void attachView(RegisterContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void register(String username, String password, String email) {
        new AuthenticationManager(view.getContext(), username, password, email)
                .register();
    }
}
