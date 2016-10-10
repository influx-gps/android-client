package com.gut.follower.utility;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.gut.follower.activities.record.RecordContract;

public class GpsProvider implements LocationListener{
    private RecordContract.Presenter presenter;
    private LocationManager locationManager;

    public GpsProvider(RecordContract.Presenter presenter) {
        this.presenter = presenter;
        this.locationManager = getLocationManagerInstance();
    }

    private LocationManager getLocationManagerInstance(){
        return (LocationManager) presenter
                .getContext()
                .getSystemService(Context.LOCATION_SERVICE);
    }

    public void start(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void stop(){
        locationManager.removeUpdates(this);
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    @Override
    public void onLocationChanged(Location newLocation) {
        presenter.postLocation(newLocation);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }
}
