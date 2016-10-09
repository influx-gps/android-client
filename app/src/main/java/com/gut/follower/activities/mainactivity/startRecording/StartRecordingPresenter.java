package com.gut.follower.activities.mainactivity.startRecording;

public class StartRecordingPresenter implements StartRecordingContract.Presenter {

    public static String TAG = "StartRecordingPresenter";

    private StartRecordingContract.View view;

    @Override
    public void attachView(StartRecordingContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

}
