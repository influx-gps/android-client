package com.gut.follower.activities.main.history;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gut.follower.R;
import com.gut.follower.activities.track.TrackActivity;
import com.gut.follower.model.Track;

import java.util.List;

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
        final Track track = trackList.get(position);
        holder.trackDistance.setText("4.35");
        holder.trackAvgSpeed.setText("15.4");
        holder.trackDuration.setText("56 : 34");
        holder.activityType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_directions_bike_black_48dp));
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TrackActivity.class);
                intent.putExtra("trackId", track.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CardView row;
        public ImageView activityType;
        public TextView trackDistance;
        public TextView trackAvgSpeed;
        public TextView trackDuration;

        public MyViewHolder(View itemView) {
            super(itemView);
            row = (CardView)itemView;
            activityType = (ImageView)itemView.findViewById(R.id.activity_type);
            trackAvgSpeed = (TextView)itemView.findViewById(R.id.track_avg_speed);
            trackDistance = ( TextView)itemView.findViewById(R.id.track_distance);
            trackDuration= ( TextView)itemView.findViewById(R.id.track_duration);
        }
    }
}
