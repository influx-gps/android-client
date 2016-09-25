package com.gut.follower.activities.mainactivity.recordfragment;


import android.content.Context;
import android.graphics.Color;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gut.follower.R;

import java.util.List;


public class RecordFragment extends Fragment implements RecordContract.View, OnMapReadyCallback {

    private RecordContract.Presenter presenter;

    private PolylineOptions options;
    private Polyline polyline;
    private GoogleMap map;

    private TextView recordingText;
    private TextView gpsStatusText;
    private Button startButton;
    private Button stopButton;

    public RecordFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        SupportMapFragment mapFragment =
                (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initFragmentVariables(view);

        return view;
    }


    private void initFragmentVariables(View view) {
        presenter = new RecordPresenter(this);

        recordingText = (TextView)view.findViewById(R.id.recording_text);
        gpsStatusText = (TextView)view.findViewById(R.id.gpsStatus_text);
        startButton = (Button)view.findViewById(R.id.start_button);
        stopButton = (Button)view.findViewById(R.id.stop_button);

        options = new PolylineOptions()
                .color(Color.BLUE)
                .width(5f);

        gpsStatusText.setText(presenter.getGpsStatus());

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
            }
        });

        stopButton.setEnabled(false);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
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

    private void stopRecording() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);

        presenter.endTrack();

        recordingText.setText("OFF");
    }

    private void startRecording() {
        recordingText.setText("ON");
        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        if (polyline != null) {
            polyline.remove();
        }

        presenter.postTrack();
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
}
