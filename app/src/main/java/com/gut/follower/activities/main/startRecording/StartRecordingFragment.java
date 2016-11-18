package com.gut.follower.activities.main.startRecording;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.gut.follower.R;
import com.gut.follower.activities.record.RecordActivity;
import com.gut.follower.utility.ApplicationConstants;


public class StartRecordingFragment extends Fragment implements StartRecordingContract.View,
        OnMapReadyCallback, View.OnClickListener {

    private StartRecordingContract.Presenter mPresenter;

    private GoogleMap map;

    private Button startButton;
    private ImageButton runMode;
    private ImageButton bikeMode;
    private FloatingActionsMenu fabMenu;
    private FloatingActionButton terrain;
    private FloatingActionButton normal;
    private FloatingActionButton satellite;

    public StartRecordingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_recording, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initFragmentVariables(view);
        setRunMode();

        return view;
    }


    private void initFragmentVariables(View view) {
        mPresenter = new StartRecordingPresenter();
        mPresenter.attachView(this);

        fabMenu = (FloatingActionsMenu) view.findViewById(R.id.fab_menu);
        normal = (FloatingActionButton) view.findViewById(R.id.normal_mode);
        terrain = (FloatingActionButton) view.findViewById(R.id.terrain_mode);
        satellite = (FloatingActionButton) view.findViewById(R.id.satellite_mode);

        runMode = (ImageButton) view.findViewById(R.id.run_mode);
        bikeMode = (ImageButton) view.findViewById(R.id.bike_mode);
        startButton = (Button) view.findViewById(R.id.start_button);

        startButton.setOnClickListener(this);
        runMode.setOnClickListener(this);
        bikeMode.setOnClickListener(this);
        normal.setOnClickListener(this);
        terrain.setOnClickListener(this);
        satellite.setOnClickListener(this);
    }

    private void setRunMode() {
        mPresenter.setMode(ApplicationConstants.RUN_MODE);
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void startRecordingActivity(String mode) {
        if (getGpsStatus()) {
            Intent intent = new Intent(getContext(), RecordActivity.class);
            intent.putExtra(ApplicationConstants.BUNDLE_MODE, mode);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Turn on gps", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void selectRunMode() {
        runMode.getBackground().setColorFilter(getResources().getColor(R.color.red_light), PorterDuff.Mode.SRC_ATOP);
        bikeMode.getBackground().setColorFilter(getResources().getColor(R.color.grey_light), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void selectBikeMode() {
        runMode.getBackground().setColorFilter(getResources().getColor(R.color.grey_light), PorterDuff.Mode.SRC_ATOP);
        bikeMode.getBackground().setColorFilter(getResources().getColor(R.color.red_light), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);

        LocationManager locationManager =
                (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location =
                locationManager
                        .getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        if (location != null) {
            map.moveCamera(
                    CameraUpdateFactory
                            .newLatLngZoom(new LatLng(location.getLatitude(),
                                            location.getLongitude()),
                                    15));
        }
    }

    public boolean getGpsStatus() {
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.run_mode:
                mPresenter.setMode(ApplicationConstants.RUN_MODE);
                break;
            case R.id.bike_mode:
                mPresenter.setMode(ApplicationConstants.BIKE_MODE);
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
            case R.id.start_button:
                mPresenter.startRecording();
                break;
        }
    }
}
