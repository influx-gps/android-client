package com.gut.follower.activities.main.savedtracks;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gut.follower.R;
import com.gut.follower.commons.DateConverter;
import com.gut.follower.activities.track.TrackActivity;
import com.gut.follower.model.Track;
import com.gut.follower.utility.ApplicationConstants;

import java.util.List;

public class SavedTracksListAdapter extends RecyclerView.Adapter<SavedTracksListAdapter.MyViewHolder>{

    private Context mContext;
    private List<Track> trackList;
    private String lastDate;

    public SavedTracksListAdapter(Context mContext, List<Track> trackList) {
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
        holder.trackDistance.setText(String.format("%.2f", track.getDistance()).replace(",", "."));
        setDate(holder, track.getStartTime());
        holder.trackAvgSpeed.setText(String.format("%.2f", track.getAvgSpeed()).replace(",", "."));
        holder.trackDuration.setText(getFormattedTime(track.getStartTime(), track.getFinishTime()));
        setActivity(holder, track.getActivity());
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TrackActivity.class);
                intent.putExtra(ApplicationConstants.BUNDLE_TRACK_ID, track.getId());
                mContext.startActivity(intent);
            }
        });
    }

    private void setActivity(MyViewHolder holder, String activity) {
        if(ApplicationConstants.BIKE_MODE.equals(activity)){
            holder.activityType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_directions_bike_black_48dp));
        } else {
            holder.activityType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_directions_run_black_48dp));
        }
    }

    private String getFormattedTime(long startTime, long finishTime) {
        return DateConverter.convertToTime(startTime, finishTime);
    }

    private void setDate(MyViewHolder holder, long startTime) {
        if(lastDate == null || isNewDate(startTime)){
            holder.trackDate.setVisibility(View.VISIBLE);
            String date = DateConverter.convertDate(startTime);
            holder.trackDate.setText(date);
            lastDate = date;
        } else {
            holder.trackDate.setVisibility(View.GONE);
        }
    }

    private boolean isNewDate(long startTime) {
        String newDate = DateConverter.convertDate(startTime);
        if(!newDate.equals(lastDate)){
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout row;
        public ImageView activityType;
        public TextView trackDistance;
        public TextView trackAvgSpeed;
        public TextView trackDuration;
        public TextView trackDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            row = (LinearLayout)itemView;
            activityType = (ImageView)itemView.findViewById(R.id.activity_type);
            trackAvgSpeed = (TextView)itemView.findViewById(R.id.track_avg_speed);
            trackDistance = ( TextView)itemView.findViewById(R.id.track_distance);
            trackDuration= ( TextView)itemView.findViewById(R.id.track_duration);
            trackDate = (TextView)itemView.findViewById(R.id.track_date);
        }
    }
}
