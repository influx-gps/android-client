package com.gut.follower.activities.main.startRecording;


import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.gut.follower.R;
import com.gut.follower.activities.record.RecordActivity;


public class StartRecordingFragment extends Fragment implements StartRecordingContract.View, OnMapReadyCallback {

    private StartRecordingContract.Presenter mPresenter;

    private GoogleMap map;

    private TextView gpsStatusText;
    private Button startButton;

    public StartRecordingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_recording, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initFragmentVariables(view);

        return view;
    }


    private void initFragmentVariables(View view) {
        mPresenter = new StartRecordingPresenter();
        mPresenter.attachView(this);

        gpsStatusText = (TextView) view.findViewById(R.id.gpsStatus_text);
        startButton = (Button) view.findViewById(R.id.start_button);
        gpsStatusText.setText(String.valueOf(getGpsStatus()));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
            }
        });
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    private void startRecording() {
        if (getGpsStatus()) {
            Intent intent = new Intent(getContext(), RecordActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Turn on gps", Toast.LENGTH_SHORT).show();
        }
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
