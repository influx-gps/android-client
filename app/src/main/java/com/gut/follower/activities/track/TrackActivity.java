package com.gut.follower.activities.track;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
import com.gut.follower.commons.LocationConverter;
import com.gut.follower.model.Track;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        // Create toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle("12.06.2016");
        }


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViewVariables();

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
//                mPresenter.deleteTrack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViewVariables() {
        mPresenter = new TrackPresenter();
        mPresenter.attachView(this);
        options = new PolylineOptions()
                .color(Color.BLUE)
                .width(7f);

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
