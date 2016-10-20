package com.gut.follower.activities.main.startRecording;

import com.gut.follower.activities.BasePresenter;
import com.gut.follower.activities.BaseView;

public interface StartRecordingContract {

    interface View extends BaseView {

        void startRecordingActivity(String mode);

        void selectRunMode();

        void selectBikeMode();

    }

    interface Presenter extends BasePresenter<View>{

        void setMode(String mode);

        void startRecording();

    }

}
