package com.gut.follower.activities.main.startRecording;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.gut.follower.R;
import com.gut.follower.activities.record.RecordActivity;
import com.gut.follower.utility.ApplicationConstants;


public class StartRecordingFragment extends Fragment implements StartRecordingContract.View, OnMapReadyCallback {

    private StartRecordingContract.Presenter mPresenter;

    private GoogleMap map;

    private Button startButton;
    private ImageButton runMode;
    private ImageButton bikeMode;

    public StartRecordingFragment() {}

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

        runMode = (ImageButton)view.findViewById(R.id.run_mode);
        bikeMode = (ImageButton)view.findViewById(R.id.bike_mode);
        startButton = (Button) view.findViewById(R.id.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.startRecording();
            }
        });
        runMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.setMode(ApplicationConstants.RUN_MODE);
            }
        });
        bikeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.setMode(ApplicationConstants.BIKE_MODE);
            }
        });
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
            intent.putExtra(mode, ApplicationConstants.BUNDLE_MODE);
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

        if(location != null){
            map.moveCamera(
                    CameraUpdateFactory
                            .newLatLngZoom(new LatLng(location.getLatitude(),
                                                      location.getLongitude()),
                                                      15));
        }
    }

    public boolean getGpsStatus() {
        LocationManager lm = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
