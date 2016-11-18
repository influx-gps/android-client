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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
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
import com.gut.follower.model.Track;
import com.gut.follower.utility.ApplicationConstants;

import java.util.List;

public class RecordActivity extends BaseActivity implements RecordContract.View, OnMapReadyCallback, View.OnClickListener{

    private RecordContract.Presenter mPresenter;

    private PolylineOptions options;

    private Polyline polyline;
    private GoogleMap map;
    private Button mStopButton;
    private TextView distance;
    private TextView avgSpeed;
    private Chronometer mChronometer;

    private FloatingActionsMenu fabMenu;
    private FloatingActionButton terrain;
    private FloatingActionButton normal;
    private FloatingActionButton satellite;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        // Create toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViewVariables();
        startRecording(getIntent().getExtras().getString(ApplicationConstants.BUNDLE_MODE));
    }

    private void initViewVariables() {
        mPresenter = new RecordPresenter(this);
        mStopButton = (Button)findViewById(R.id.stop_recording);
        distance = (TextView)findViewById(R.id.distance);
        avgSpeed = (TextView)findViewById(R.id.record_track_avg_speed);
        options = new PolylineOptions()
                .color(getResources().getColor(R.color.track_color))
                .width(8f);
        mChronometer = (Chronometer)findViewById(R.id.chronometer);
        fabMenu = (FloatingActionsMenu)findViewById(R.id.fab_menu);
        normal = (FloatingActionButton)findViewById(R.id.normal_mode);
        terrain = (FloatingActionButton)findViewById(R.id.terrain_mode);
        satellite = (FloatingActionButton)findViewById(R.id.satellite_mode);

        mStopButton.setOnClickListener(this);
        normal.setOnClickListener(this);
        terrain.setOnClickListener(this);
        satellite.setOnClickListener(this);
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
        new AlertDialog.Builder(getApplicationContext())
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

    @Override
    public void setDistance(Double distance) {
        this.distance.setText(String.format("%.2f", distance).replace(",", "."));
    }

    @Override
    public void startStopper() {
        mChronometer.start();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTrackInfo(Track track) {
        setValue(distance,track.getDistance());
        setValue(avgSpeed, track.getAvgSpeed());
    }

    private void setValue(TextView textView, Double value) {
        if (value != null) {
            textView.setText(String.format("%.2f", value).replace(",","."));
        } else {
            textView.setText("0.00");
        }
    }

    private void stopRecording() {
        mPresenter.endTrack();
    }

    private void startRecording(String activity) {
        mPresenter.postTrack(activity);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stop_recording:
                stopRecording();
                break;
            case R.id.normal_mode:
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                fabMenu.collapse();
                break;
            case R.id.terrain_mode:
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                fabMenu.collapse();
                break;
            case R.id.satellite_mode:
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                fabMenu.collapse();
                break;
        }
    }
}
