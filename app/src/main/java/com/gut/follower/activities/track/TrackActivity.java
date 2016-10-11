package com.gut.follower.activities.track;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gut.follower.R;
import com.gut.follower.activities.BaseActivity;
import com.gut.follower.activities.record.RecordPresenter;
import com.gut.follower.commons.LocationConverter;
import com.gut.follower.model.Track;

import java.util.List;

public class TrackActivity extends BaseActivity implements TrackContract.View, OnMapReadyCallback {

    private TrackContract.Presenter mPresenter;

    private GoogleMap map;
    private PolylineOptions options;

    private TextView mNormalMap;
    private TextView mSatelliteMap;
    private TextView mTerrainMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViewVariables();

        String trackId = getIntent().getStringExtra("trackId");
        mPresenter.loadTrack(trackId);
    }

    private void initViewVariables() {
        mPresenter = new TrackPresenter();
        mPresenter.attachView(this);
        options = new PolylineOptions()
                .color(Color.BLUE)
                .width(7f);
        mNormalMap = (TextView)findViewById(R.id.normal);
        mSatelliteMap = (TextView)findViewById(R.id.satellite);
        mTerrainMap = (TextView)findViewById(R.id.terrain);
        mNormalMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });

        mSatelliteMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });
        mTerrainMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showTrackInfo(Track track) {
        drawTrackOnMap(LocationConverter.getLatLngLocations(track.getLocations()));
        //TODO: show some track information
    }


    private void drawTrackOnMap(List<LatLng> positions) {
        options.addAll(positions);
        map.addPolyline(options);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(LatLng location: positions){
            builder.include(location);
        }

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 15);
        map.moveCamera(cameraUpdate);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }
}
