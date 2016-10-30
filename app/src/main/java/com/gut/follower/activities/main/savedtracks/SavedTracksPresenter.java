package com.gut.follower.activities.main.savedtracks;

import com.gut.follower.model.Track;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;
import com.gut.follower.utility.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedTracksPresenter implements SavedTracksContract.Presenter{

    public static String TAG = "SavedTracksPresenter";

    private SavedTracksContract.View view;

    @Override
    public void loadTracks() {
        JConductorService service = ServiceGenerator
                .createService(JConductorService.class,
                        SessionManager.getUsername(),
                        SessionManager.getPassword());

        Call<List<Track>> call = service.getTracks();
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful()) {
                    if (view != null) {
                        view.showTrackList(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {

            }
        });
    }

    @Override
    public void start() {

    }

    @Override
    public void attachView(SavedTracksContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
