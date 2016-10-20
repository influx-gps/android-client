package com.gut.follower.activities.main.startRecording;

import com.gut.follower.utility.ApplicationConstants;

public class StartRecordingPresenter implements StartRecordingContract.Presenter {

    public static String TAG = "StartRecordingPresenter";

    private StartRecordingContract.View view;
    private String mode;

    @Override
    public void attachView(StartRecordingContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void setMode(String mode) {
        this.mode = mode;
        if(ApplicationConstants.BIKE_MODE.equals(mode)){
            view.selectBikeMode();
        } else if(ApplicationConstants.RUN_MODE.equals(mode)){
            view.selectRunMode();
        }
    }

    @Override
    public void startRecording() {
        view.startRecordingActivity(mode);
    }
}
