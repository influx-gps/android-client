package com.gut.follower.activities.mainactivity.userPanelFragment;

import com.gut.follower.utility.AuthenticationManager;

public class UserPanelPresenter implements UserPanelContract.Presenter{

    public static String TAG = "UserPanelPresenter";

    private UserPanelContract.View view;

    @Override
    public void logout() {
        AuthenticationManager.logout(view.getContext());
    }

    @Override
    public void changePassword(String password) {

    }

    @Override
    public void changeEmail(String email) {

    }

    @Override
    public void changeUsername(String username) {

    }

    @Override
    public void attachView(UserPanelContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
