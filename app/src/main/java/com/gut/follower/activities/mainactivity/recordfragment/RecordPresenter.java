package com.gut.follower.activities.mainactivity.recordfragment;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.gut.follower.R;
import com.gut.follower.model.GutLocation;
import com.gut.follower.model.Track;
import com.gut.follower.utility.GpsProvider;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;
import com.gut.follower.utility.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gut.follower.commons.LocationConverter.getGutLocation;
import static com.gut.follower.commons.LocationConverter.getLatLngLocations;

public class RecordPresenter implements RecordContract.Presenter {

    public static String TAG = "RecordPresenter";

    private GpsProvider gpsProvider;

    private RecordContract.View recordMvpView;
    private Track track;
    private Location location;

    public RecordPresenter(RecordContract.View view) {
        this.recordMvpView = view;
        this.gpsProvider = new GpsProvider(this);
    }

    @Override
    public void attachView(RecordContract.View view) {
        this.recordMvpView = view;
    }

    @Override
    public void detachView() {
        this.recordMvpView = null;
    }

    public void postTrack(){
        gpsProvider.start();
        location = getLastLocation();
        if (location != null) {
            JConductorService jConductorService = getRestService();
            Call<Track> call = jConductorService.postTrack(getGutLocation(location));
            call.enqueue(new Callback<Track>() {
                @Override
                public void onResponse(Call<Track> call, Response<Track> response) {
                    if (response.isSuccessful()) {
                        track = response.body();
                        recordMvpView.drawTrackOnMap(getLatLngLocations(response.body().getLocations()));
                        Toast.makeText(recordMvpView.getContext(),
                                track.getId(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(recordMvpView.getContext(),
                                response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Track> call, Throwable t) {
                    Toast.makeText(recordMvpView.getContext(),
                            t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(recordMvpView.getContext(),
                    recordMvpView.getContext().getResources().getString(R.string.noLocation),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void postLocation(Location location){
        if (track != null) {
            JConductorService jConductorService = getRestService();
            Call<Track> call = jConductorService.postLocation(track.getId(), getGutLocation(location), false);

            call.enqueue(new Callback<Track>() {
                @Override
                public void onResponse(Call<Track> call, Response<Track> response) {
                    if (response.isSuccessful()) {
                        recordMvpView.drawTrackOnMap(getLatLngLocations(response.body().getLocations()));
                    } else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Track> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void endTrack(){
        gpsProvider.stop();
        location = getLastLocation();
        JConductorService jConductorService = getRestService();
        if (track != null) {
            Call<Track> call = jConductorService.postLocation(
                    track.getId(),
                    getGutLocation(location),
                    true);
            call.enqueue(new Callback<Track>() {
                @Override
                public void onResponse(Call<Track> call, Response<Track> response) {
                    if (response.isSuccessful()) {
                        track = response.body();
                        recordMvpView.drawTrackOnMap(getLatLngLocations(track.getLocations()));
                        Toast.makeText(recordMvpView.getContext(),
                                "Tracked saved",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Track> call, Throwable t) {
                    Toast.makeText(recordMvpView.getContext(),
                            t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public String getGpsStatus(){
        return String.valueOf(
                gpsProvider.getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER));
    }

    public Context getContext(){
        return recordMvpView.getContext();
    }

    private Location getLastLocation(){
        return gpsProvider
                .getLocationManager()
                .getLastKnownLocation
                        (gpsProvider
                                .getLocationManager()
                                .getBestProvider(new Criteria(), false));
    }

    private JConductorService getRestService(){
        return ServiceGenerator
                .createService(JConductorService.class,
                        SessionManager.getUsername(recordMvpView.getContext()),
                        SessionManager.getPassword(recordMvpView.getContext()));
    }
}
