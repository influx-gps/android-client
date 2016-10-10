package com.gut.follower.activities.login;

import com.gut.follower.activities.BasePresenter;
import com.gut.follower.activities.BaseView;

public interface LoginContract {

    interface View extends BaseView{
        void showLoadingSpinner();

        void hideLoadingSpinner();

        void hideUserLoginForm();

        void showUserLoginForm();

        void showToast(String message);

        void startMainActivity();

        void hideKeyboard();
    }

    interface Presenter extends BasePresenter<View>{

        void login(String username, String password);

    }
}
