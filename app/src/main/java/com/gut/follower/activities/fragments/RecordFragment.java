package com.gut.follower.activities.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.gut.follower.model.GutLocation;
import com.gut.follower.model.Track;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecordFragment extends Fragment implements OnMapReadyCallback {

    private JConductorService jConductorService;
    private GpsProvider gpsProvider;

    private GoogleMap map;
    private Button startButton;
    private Button stopButton;
    private TextView gpsStatusText;
    private TextView recordingText;

    private boolean isRecording = false;

    private Location location;
    private String trackId;

    PolylineOptions options;
    Polyline polyline;
    List<LatLng> positions;

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

        recordingText = (TextView)view.findViewById(R.id.recording_text);
        gpsStatusText = (TextView)view.findViewById(R.id.gpsStatus_text);
        startButton = (Button)view.findViewById(R.id.start_button);
        stopButton = (Button)view.findViewById(R.id.stop_button);

        options = new PolylineOptions()
                .color(Color.BLUE)
                .width(5f);
        positions = new LinkedList<>();

        jConductorService = ServiceGenerator
                .createService(JConductorService.class, BuildConfig.USERNAME, BuildConfig.PASSWORD);
        gpsProvider = new GpsProvider(getContext());


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

        return view;
    }

    private void stopRecording() {

        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        isRecording = false;


        if (location != null) {
            Call<Track> call = jConductorService.postLocation(trackId,
                                                 gpsProvider.provideLocationData(location),
                                                 true);
            call.enqueue(new Callback<Track>() {
                @Override
                public void onResponse(Call<Track> call, Response<Track> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(),
                                       "Tracked saved",
                                       Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Track> call, Throwable t) { }
            });

        }
        recordingText.setText("OFF");
        gpsProvider.stop();
    }

    private void startRecording() {
        gpsProvider.start();
        isRecording = true;
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
            positions = new LinkedList<>();
        }
        positions.add(new LatLng(location.getLatitude(), location.getLongitude()));
        options.addAll(positions);
        polyline = map.addPolyline(options);

        Call<Track> call = jConductorService.postTrack(gpsProvider.provideLocationData(location));
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if (response.isSuccessful()) {
                    trackId = response.body().getId();
                    Toast.makeText(getContext(),
                                   trackId,
                                   Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(),
                                   response.message(),
                                   Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                Toast.makeText(getContext(),
                               t.getMessage(),
                               Toast.LENGTH_SHORT).show();
            }
        });
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
                    .zoom(13) // Sets the zoom
                    .build(); // Creates a CameraPosition from the builder
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private class GpsProvider implements LocationListener{
    // TODO: This class should be separated.
        private Context mContext;

        private LocationManager locationManager;

        public GpsProvider(Context mContext) {
            this.mContext = mContext;
            this.locationManager =
                    (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        }

        public GutLocation provideLocationData(Location location){
                return new GutLocation(location.getLatitude(),
                        location.getLongitude(),
                        location.getTime());
        }

        public void start(){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5, 3, this);
        }

        public void stop(){
            locationManager.removeUpdates(this);
        }

        public LocationManager getLocationManager() {
            return locationManager;
        }

        @Override
        public void onLocationChanged(Location newLocation) {
            if(isRecording){
                location = newLocation;
                GutLocation gutLocation =
                        new GutLocation(newLocation.getLatitude(),
                                        newLocation.getLongitude(),
                                        newLocation.getTime());

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                positions.add(latLng);
                polyline.setPoints(positions);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                map.animateCamera(cameraUpdate);

                Call<Track> call = jConductorService.postLocation(trackId, gutLocation, false);

                call.enqueue(new Callback<Track>() {
                    @Override
                    public void onResponse(Call<Track> call, Response<Track> response) {

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
        }

        @Override
        public void onProviderDisabled(String s) {
            gpsStatusText.setText("OFF");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }
}
