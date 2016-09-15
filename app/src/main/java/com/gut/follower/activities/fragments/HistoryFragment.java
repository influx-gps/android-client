package com.gut.follower.activities.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gut.follower.BuildConfig;
import com.gut.follower.R;
import com.gut.follower.adapters.TrackListAdapter;
import com.gut.follower.model.Track;
import com.gut.follower.utility.JConductorService;
import com.gut.follower.utility.ServiceGenerator;
import com.gut.follower.utility.SessionManager;
import com.gut.follower.utility.UserCredentials;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TrackListAdapter mAdapter;
    private List<Track> trackList;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        loadTracks();


        return view;
    }

    private void loadTracks() {
        JConductorService service = ServiceGenerator
                .createService(JConductorService.class,
                        SessionManager.getUsername(getContext()),
                        SessionManager.getPassword(getContext()));

        Call<List<Track>> call = service.getTracks();
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                trackList = response.body();
                mAdapter = new TrackListAdapter(getContext(), trackList);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Track>> call, Throwable t) {

            }
        });
    }

}
