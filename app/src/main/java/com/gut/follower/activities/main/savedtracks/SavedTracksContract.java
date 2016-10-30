package com.gut.follower.activities.main.savedtracks;

import com.gut.follower.activities.BasePresenter;
import com.gut.follower.activities.BaseView;
import com.gut.follower.model.Track;

import java.util.List;

public interface SavedTracksContract {

    interface View extends BaseView{

        void showTrackList(List<Track> trackList);

    }

    interface Presenter extends BasePresenter<View>{

        void loadTracks();
    }
}
