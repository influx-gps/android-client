package com.gut.follower.activities.track;

import com.gut.follower.model.Track;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;
import com.gut.follower.utility.SessionManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackPresenter implements TrackContract.Presenter{

    private TrackContract.View view;
    private String trackId;

    @Override
    public void loadTrack(final String trackId) {
        this.trackId = trackId;
        JConductorService restApi = ServiceGenerator
                .createService(JConductorService.class,
                        SessionManager.getUsername(),
                        SessionManager.getPassword());
        Call<Track> call = restApi.getTrack(trackId);
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if(response.isSuccessful()){
                    view.showTrackInfo(response.body());
                } else {
                    view.showToast(response.message());
                    view.finishActivity();
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                view.showToast(t.getMessage());
                view.finishActivity();
            }
        });
    }

    @Override
    public void deleteTrack() {
        JConductorService restApi = ServiceGenerator
                .createService(JConductorService.class,
                        SessionManager.getUsername(),
                        SessionManager.getPassword());
        Call<ResponseBody> call = restApi.deleteTrack(trackId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    view.startMainActivity();
                } else {
                    view.showToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.showToast(t.getMessage());
            }
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void attachView(TrackContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
