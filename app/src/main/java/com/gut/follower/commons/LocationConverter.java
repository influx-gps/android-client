package com.gut.follower.commons;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.gut.follower.model.GutLocation;

import java.util.LinkedList;
import java.util.List;

public class LocationConverter {
    public static List<LatLng> getLatLngLocations(List<GutLocation> locations){
        List<LatLng> newLocations = new LinkedList<>();
        for(GutLocation location: locations){
            newLocations.add(new LatLng(location.getLatitude(), location.getLongitude()));
        }
        return newLocations;
    }

    public static GutLocation getGutLocation(Location location){
        return new GutLocation(location.getLatitude(),
                location.getLongitude(),
                location.getTime());
    }
}
