package com.gut.follower.activities.fragments;


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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gut.follower.BuildConfig;
import com.gut.follower.R;
import com.gut.follower.utility.GpsProvider;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.RecordView;
import com.gut.follower.utility.ServiceGenerator;
import com.gut.follower.utility.TrackService;

import java.util.Arrays;
import java.util.List;


public class RecordFragment extends Fragment implements RecordView, OnMapReadyCallback {

    private JConductorService jConductorService;
    private GpsProvider gpsProvider;

    private GoogleMap map;
    private Button startButton;
    private Button stopButton;
    private TextView gpsStatusText;
    private TextView recordingText;

    private Location location;
    private String trackId;

    PolylineOptions options;
    Polyline polyline;

    public RecordFragment() {
        // Required empty public constructor
    }


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
        recordingText = (TextView)view.findViewById(R.id.recording_text);
        gpsStatusText = (TextView)view.findViewById(R.id.gpsStatus_text);
        startButton = (Button)view.findViewById(R.id.start_button);
        stopButton = (Button)view.findViewById(R.id.stop_button);

        options = new PolylineOptions()
                .color(Color.BLUE)
                .width(5f);

        jConductorService = ServiceGenerator
                .createService(JConductorService.class, BuildConfig.USERNAME, BuildConfig.PASSWORD);
        gpsProvider = new GpsProvider(this);


        gpsStatusText.setText(String.valueOf(
                gpsProvider.getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)));

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
    public void drawTrackOnMap(List<LatLng> locations) {
        polyline.setPoints(locations);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(locations.get(locations.size()-1), 15);
        map.animateCamera(cameraUpdate);
    }

    public String getTrackId() {
        return trackId;
    }

    @Override
    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    private void stopRecording() {

        startButton.setEnabled(true);
        stopButton.setEnabled(false);


        if (location != null) {
            new TrackService(this).endTrack(location);
        }
        recordingText.setText("OFF");
        gpsProvider.stop();
    }

    private void startRecording() {
        gpsProvider.start();
        recordingText.setText("ON");
        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        location = gpsProvider
                .getLocationManager()
                .getLastKnownLocation
                        (gpsProvider
                                .getLocationManager()
                                .getBestProvider(new Criteria(), false));

        if (polyline != null) {
            polyline.remove();
        }

        options.addAll(Arrays.asList(new LatLng(location.getLatitude(), location.getLongitude())));
        polyline = map.addPolyline(options);

        new TrackService(this).postTrack(location);
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
            map.animateCamera(
                    CameraUpdateFactory
                            .newLatLngZoom(new LatLng(location.getLatitude(),
                                                      location.getLongitude()),
                                                      15));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    // Sets the center of the map to location user
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(15) // Sets the zoom
                    .build(); // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}
