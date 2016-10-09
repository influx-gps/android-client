package com.gut.follower.activities.loginactivity;

import com.gut.follower.activities.BasePresenter;
import com.gut.follower.activities.BaseView;

public interface LoginContract {

    interface View extends BaseView{
        void showLoadingSpinner();

        void hideUserLoginForm();
    }

    interface Presenter extends BasePresenter<View>{

        void login(String username, String password);

    }
}
