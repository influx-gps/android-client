package com.gut.follower.activities.track;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gut.follower.R;
import com.gut.follower.activities.BaseActivity;
import com.gut.follower.activities.main.MainActivity;
import com.gut.follower.commons.DateConverter;
import com.gut.follower.commons.LocationConverter;
import com.gut.follower.model.Track;
import com.gut.follower.utility.ApplicationConstants;

import java.util.List;

public class TrackActivity extends BaseActivity implements TrackContract.View, OnMapReadyCallback, View.OnClickListener {

    private TrackContract.Presenter mPresenter;

    private GoogleMap map;
    private PolylineOptions options;

    private FloatingActionsMenu fabMenu;
    private FloatingActionButton terrain;
    private FloatingActionButton normal;
    private FloatingActionButton satellite;
    private Toolbar toolbar;
    private TextView trackDistance;
    private TextView trackDuration;
    private TextView trackAvgSpeed;
    private TextView trackRunPace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        // Create toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(ApplicationConstants.EMPTY_STRING);
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViewVariables();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String trackId = getIntent().getStringExtra("trackId");
        mPresenter.loadTrack(trackId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.track_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                mPresenter.deleteTrack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViewVariables() {
        mPresenter = new TrackPresenter();
        mPresenter.attachView(this);
        options = new PolylineOptions()
                .color(getResources().getColor(R.color.track_color))
                .width(8f);

        trackAvgSpeed = (TextView)findViewById(R.id.track_avg_speed);
        trackDistance = (TextView)findViewById(R.id.track_distance);
        trackDuration = (TextView)findViewById(R.id.track_time_duration);
        trackRunPace = (TextView)findViewById(R.id.track_run_pace);
        fabMenu = (FloatingActionsMenu)findViewById(R.id.fab_menu);
        normal = (FloatingActionButton)findViewById(R.id.normal_mode);
        terrain = (FloatingActionButton)findViewById(R.id.terrain_mode);
        satellite = (FloatingActionButton)findViewById(R.id.satellite_mode);

        normal.setOnClickListener(this);
        terrain.setOnClickListener(this);
        satellite.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showTrackInfo(Track track) {
        drawTrackOnMap(LocationConverter.getLatLngLocations(track.getLocations()));
        getSupportActionBar().setTitle(DateConverter.convertDateWithTime(track.getStartTime()));
        setValue(trackDistance, track.getDistance());
        setValue(trackAvgSpeed, track.getAvgSpeed());
        trackDuration.setText(DateConverter.convertToTime(track.getStartTime(), track.getFinishTime()));
        setValue(trackRunPace, track.getRunPace());
    }

    private void setValue(TextView textView, Double value) {
        if (value != null) {
            textView.setText(String.format("%.2f", value).replace(",", "."));
        } else {
            textView.setText("n/a");
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(ApplicationConstants.BUNDLE_MAIN_ACTIVITY_TAB, "1");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
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
