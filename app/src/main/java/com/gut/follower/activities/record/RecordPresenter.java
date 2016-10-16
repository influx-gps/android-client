package com.gut.follower.activities.record;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.gut.follower.R;
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

public class RecordPresenter implements RecordContract.Presenter{

    private GpsProvider gpsProvider;

    private RecordContract.View view;
    private Track track;
    private Location location;

    public RecordPresenter(RecordContract.View view) {
        this.view = view;
        this.gpsProvider = new GpsProvider(this);
    }

    @Override
    public void attachView(RecordContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
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
                        view.drawTrackOnMap(getLatLngLocations(response.body().getLocations()));
                        view.setDistance(response.body().getDistance());
                        Toast.makeText(view.getContext(),
                                track.getId(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(view.getContext(),
                                response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Track> call, Throwable t) {
                    Toast.makeText(view.getContext(),
                            t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(view.getContext(),
                    view.getContext().getResources().getString(R.string.noLocation),
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
                        view.drawTrackOnMap(getLatLngLocations(response.body().getLocations()));
                        view.setDistance(response.body().getDistance());
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
                        view.drawTrackOnMap(getLatLngLocations(track.getLocations()));
                        Toast.makeText(view.getContext(),
                                "Tracked saved",
                                Toast.LENGTH_SHORT).show();
                        view.startTrackActivity(track.getId());
                        view.finishActivity();
                    }
                }
                @Override
                public void onFailure(Call<Track> call, Throwable t) {
                    Toast.makeText(view.getContext(),
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
        return view.getContext();
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
                        SessionManager.getUsername(view.getContext()),
                        SessionManager.getPassword(view.getContext()));
    }
}
