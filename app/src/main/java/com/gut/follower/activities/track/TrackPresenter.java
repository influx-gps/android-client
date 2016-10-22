package com.gut.follower.activities.track;

import com.gut.follower.model.Track;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;
import com.gut.follower.utility.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackPresenter implements TrackContract.Presenter{

    private TrackContract.View view;

    @Override
    public void loadTrack(String trackId) {
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

                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {

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
