package com.gut.follower.utility;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface RecordView {

    void drawTrackOnMap(List<LatLng> locations);

    String getTrackId();

    void setTrackId(String trackId);

    Context getContext();
}
