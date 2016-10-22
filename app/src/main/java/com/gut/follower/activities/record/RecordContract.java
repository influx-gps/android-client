package com.gut.follower.activities.record;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.gut.follower.activities.BasePresenter;
import com.gut.follower.activities.BaseView;

import java.util.List;

public interface RecordContract {

    interface View extends BaseView{

        void drawTrackOnMap(List<LatLng> locations);

        void finishActivity();

        void startTrackActivity(String id);

        void setDistance(Double distance);

        void startStopper();

        void showToast(String message);
    }

    interface Presenter extends BasePresenter<View>{

        void postTrack(String activity);

        void postLocation(Location location);

        void endTrack();

    }
}
