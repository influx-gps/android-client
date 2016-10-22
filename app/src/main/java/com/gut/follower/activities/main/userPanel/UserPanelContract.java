package com.gut.follower.activities.main.userPanel;

import com.gut.follower.activities.BasePresenter;
import com.gut.follower.activities.BaseView;
import com.gut.follower.model.Account;

public interface UserPanelContract {

    interface View extends BaseView{

        void showToast(String message);

        void setUserInfo(Account account);
    }

    interface Presenter extends BasePresenter<View>{

        void logout();

        void loadUserInfo();

        void editEmail(String email);

        void editPassword(String password);
    }
}
