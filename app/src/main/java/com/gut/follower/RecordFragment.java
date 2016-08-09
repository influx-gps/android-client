package com.gut.follower;


import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.gut.follower.model.GutLocation;
import com.gut.follower.model.Track;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private JConductorService jConductorService;

    private GoogleMap map;
    private Button startButton;
    private Button stopButton;
    private TextView gpsStatusText;
    private TextView recordingText;

    private boolean isRecording = false;

    private LocationManager locationManager;
    private Location location;
    private String trackId;

    public RecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        recordingText = (TextView)view.findViewById(R.id.recording_text);
        gpsStatusText = (TextView)view.findViewById(R.id.gpsStatus_text);

        jConductorService = ServiceGenerator.createService(JConductorService.class, "adam", "adamg");

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                this
        );

        gpsStatusText.setText(String.valueOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));

        startButton = (Button)view.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecording = true;
                recordingText.setText("ON");
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
                location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
                GutLocation gutLocation = new GutLocation(location.getLatitude(), location.getLongitude(), location.getTime());
                Call<Track> call = jConductorService.postTrack(gutLocation);
                call.enqueue(new Callback<Track>() {
                    @Override
                    public void onResponse(Call<Track> call, Response<Track> response) {
                        if (response.isSuccessful()) {
                            trackId = response.body().getId();
                            Toast.makeText(getContext(), trackId, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Track> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        stopButton = (Button)view.findViewById(R.id.stop_button);
        stopButton.setEnabled(false);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                isRecording = false;
                if (location != null) {
                    GutLocation gutLocation = new GutLocation(location.getLatitude(), location.getLongitude(), location.getTime());
                    Call<Track> call = jConductorService.postLocation(trackId, gutLocation, true);
                    call.enqueue(new Callback<Track>() {
                        @Override
                        public void onResponse(Call<Track> call, Response<Track> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Tracked saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Track> call, Throwable t) {

                        }
                    });

                }
                recordingText.setText("OFF");
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if(location != null){
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(13)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(isRecording){
            this.location = location;
            GutLocation gutLocation = new GutLocation(location.getLatitude(), location.getLongitude(), location.getTime());
            Call<Track> call = jConductorService.postLocation(trackId, gutLocation, false);
            call.enqueue(new Callback<Track>() {
                @Override
                public void onResponse(Call<Track> call, Response<Track> response) {
                    Toast.makeText(getContext(), "location sent", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<Track> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        gpsStatusText.setText("ON");
        Toast.makeText(getContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        gpsStatusText.setText("OFF");
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }
}
