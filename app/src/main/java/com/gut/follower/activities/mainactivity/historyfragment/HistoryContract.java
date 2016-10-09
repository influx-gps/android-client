package com.gut.follower.activities.mainactivity.historyfragment;

import com.gut.follower.activities.BasePresenter;
import com.gut.follower.activities.BaseView;
import com.gut.follower.model.Track;

import java.util.List;

public interface HistoryContract {

    interface View extends BaseView{

        void showTrackList(List<Track> trackList);

    }

    interface Presenter extends BasePresenter<View>{

        void loadTracks();
    }
}
