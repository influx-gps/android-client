package com.gut.follower.activities.main.history;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gut.follower.R;
import com.gut.follower.model.Track;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements HistoryContract.View {

    private RecyclerView mRecyclerView;
    private TextView mNoResults;
    private ProgressBar mProgressBar;
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

        mNoResults = (TextView)view.findViewById(R.id.no_results);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycleView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.loadTracks();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showTrackList(List<Track> trackList) {
        mProgressBar.setVisibility(View.GONE);
        if (!trackList.isEmpty()) {
            mNoResults.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new TrackListAdapter(getContext(), trackList);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoResults.setVisibility(View.VISIBLE);
            mNoResults.setText(getString(R.string.no_tracks));
        }
    }
}
