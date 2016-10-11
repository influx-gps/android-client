package com.gut.follower.activities.record;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gut.follower.R;
import com.gut.follower.activities.BaseActivity;
import com.gut.follower.activities.track.TrackActivity;
import com.gut.follower.utility.ApplicationConstants;

import java.util.List;

public class RecordActivity extends BaseActivity implements RecordContract.View, OnMapReadyCallback{

    private RecordContract.Presenter mPresenter;

    private PolylineOptions options;

    private Polyline polyline;
    private GoogleMap map;
    private Button mStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViewVariables();
        startRecording();
    }

    private void initViewVariables() {
        mPresenter = new RecordPresenter(this);
        mStopButton = (Button)findViewById(R.id.stop_recording);
        options = new PolylineOptions()
                .color(Color.BLUE)
                .width(5f);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location =
                locationManager
                        .getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        if(location != null){
            map.moveCamera(
                    CameraUpdateFactory
                            .newLatLngZoom(new LatLng(location.getLatitude(),
                                            location.getLongitude()),
                                    15));
        }
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage("This will end recording your track. Are you sure?")
                .setPositiveButton("End recording", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.endTrack();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setTitle("Recording ON")
                .show();
    }

    @Override
    public void drawTrackOnMap(List<LatLng> locations) {
        if (polyline == null) {
            options.addAll(locations);
            polyline = map.addPolyline(options);
        } else {
            polyline.setPoints(locations);
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(locations.get(locations.size()-1), 15);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void startTrackActivity(String id) {
        Intent intent = new Intent(getApplicationContext(), TrackActivity.class);
        intent.putExtra(ApplicationConstants.BUNDLE_TRACK_ID, id);
        startActivity(intent);
    }

    private void stopRecording() {
        mPresenter.endTrack();
    }

    private void startRecording() {
        mPresenter.postTrack();
    }
}
