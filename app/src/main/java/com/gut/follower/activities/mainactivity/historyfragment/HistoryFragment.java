package com.gut.follower.activities.mainactivity.historyfragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gut.follower.R;
import com.gut.follower.adapters.TrackListAdapter;
import com.gut.follower.model.Track;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements HistoryContract.View {

    private RecyclerView mRecyclerView;
    private TrackListAdapter mAdapter;
    private HistoryContract.Presenter mPresenter;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mPresenter = new HistoryPresenter();
        mPresenter.attachView(this);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mPresenter.loadTracks();
        return view;
    }

    @Override
    public void showTrackList(List<Track> trackList) {
        mAdapter = new TrackListAdapter(getContext(), trackList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
