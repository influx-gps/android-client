package com.gut.follower.utility;

import android.location.Location;
import android.widget.Toast;

import com.gut.follower.BuildConfig;
import com.gut.follower.model.GutLocation;
import com.gut.follower.model.Track;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gut.follower.commons.LocationConverter.getLatLngLocations;

public class TrackService {
    private RecordView recordView;
    private JConductorService jConductorService;

    public TrackService(RecordView recordView) {
        this.recordView = recordView;
        this.jConductorService = getRestService();
    }

    public void postLocation(Location location){
        Call<Track> call = jConductorService.postLocation(recordView.getTrackId(), getGutLocation(location), false);

        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                recordView.drawTrackOnMap(getLatLngLocations(response.body().getLocations()));
            }
            @Override
            public void onFailure(Call<Track> call, Throwable t) {

            }
        });
    }

    public void postTrack(Location location){
        Call<Track> call = jConductorService.postTrack(getGutLocation(location));
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if (response.isSuccessful()) {
                    recordView.setTrackId(response.body().getId());
                    Toast.makeText(recordView.getContext(),
                            recordView.getTrackId(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(recordView.getContext(),
                            response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Toast.makeText(recordView.getContext(),
                        t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void endTrack(Location location){
        Call<Track> call = jConductorService.postLocation(recordView.getTrackId(),
                getGutLocation(location),
                true);
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(recordView.getContext(),
                            "Tracked saved",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Track> call, Throwable t) { }
        });
    }

    private JConductorService getRestService(){
        return ServiceGenerator
                .createService(JConductorService.class, BuildConfig.USERNAME, BuildConfig.PASSWORD);
    }

    private GutLocation getGutLocation(Location location){
        return new GutLocation(location.getLatitude(),
                location.getLongitude(),
                location.getTime());
    }
}
