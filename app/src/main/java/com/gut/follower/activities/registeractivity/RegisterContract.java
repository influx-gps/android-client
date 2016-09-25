package com.gut.follower.activities.registeractivity;

import com.gut.follower.activities.BasePresenter;
import com.gut.follower.activities.BaseView;

public interface RegisterContract {

    interface View extends BaseView{

    }

    interface Presenter extends BasePresenter<View>{

        void register(String username, String password, String email);
    }

}

