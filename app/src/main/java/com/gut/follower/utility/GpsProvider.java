package com.gut.follower.utility;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GpsProvider implements LocationListener{
    private RecordView mCallerFragment;
    private LocationManager locationManager;

    public GpsProvider(RecordView mCallerFragment) {
        this.mCallerFragment = mCallerFragment;
        this.locationManager = getLocationManagerInstance();
    }

    private LocationManager getLocationManagerInstance(){
        return (LocationManager) mCallerFragment
                .getContext()
                .getSystemService(Context.LOCATION_SERVICE);
    }

    public void start(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 3, this);
    }

    public void stop(){
        locationManager.removeUpdates(this);
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    @Override
    public void onLocationChanged(Location newLocation) {
        new TrackService(mCallerFragment).postLocation(newLocation);
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
