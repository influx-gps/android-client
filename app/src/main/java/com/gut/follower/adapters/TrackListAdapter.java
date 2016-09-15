package com.gut.follower.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gut.follower.R;
import com.gut.follower.model.Track;

import java.util.List;

import static com.gut.follower.commons.LocationConverter.getLatLngLocations;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.MyViewHolder>{

    private Context mContext;
    private List<Track> trackList;

    public TrackListAdapter(Context mContext, List<Track> trackList) {
        this.mContext = mContext;
        this.trackList = trackList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.track_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Track track = trackList.get(position);
        holder.locations = getLatLngLocations(track.getLocations());
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    private String getElapsedTime(Track track) {
        long startTime = track.getStartTime();
        long finishTime = track.getFinishTime();

        long time = (finishTime - startTime) / 1000;
        return String.valueOf(time);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback{
        public GoogleMap map;
        public MapView mapView;
        public List<LatLng> locations;

        public MyViewHolder(View itemView) {
            super(itemView);
            mapView = (MapView)itemView.findViewById(R.id.map);

            mapView.onCreate(null);
            mapView.getMapAsync(this);
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;

            if(!locations.isEmpty()){
                PolylineOptions options = new PolylineOptions()
                        .color(Color.BLUE)
                        .width(5f)
                        .addAll(locations);
                map.addPolyline(options);
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for(LatLng location: locations){
                    builder.include(location);
                }

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 15);
                map.animateCamera(cameraUpdate);
            }
        }
    }
}
