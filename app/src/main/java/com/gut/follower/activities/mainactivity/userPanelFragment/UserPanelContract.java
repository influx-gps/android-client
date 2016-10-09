package com.gut.follower.activities.mainactivity.userPanelFragment;

import com.gut.follower.activities.BasePresenter;
import com.gut.follower.activities.BaseView;

public interface UserPanelContract {

    interface View extends BaseView{

    }

    interface Presenter extends BasePresenter<View>{

        void logout();

        void changePassword(String password);

        void changeEmail(String email);

        void changeUsername(String username);
    }
}
