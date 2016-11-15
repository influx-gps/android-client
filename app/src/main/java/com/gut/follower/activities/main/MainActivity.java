package com.gut.follower.activities.main;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.gut.follower.R;
import com.gut.follower.activities.BaseActivity;
import com.gut.follower.activities.main.savedtracks.SavedTracksFragment;
import com.gut.follower.activities.main.startRecording.StartRecordingFragment;
import com.gut.follower.activities.main.userPanel.UserPanelFragment;
import com.gut.follower.utility.ApplicationConstants;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;


public class MainActivity extends BaseActivity {

    private BottomBar bottomBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItems(R.menu.three_buttons_menu);
        Bundle extras = getIntent().getExtras();
        if(extras != null && "1".equals(extras.getString(ApplicationConstants.BUNDLE_MAIN_ACTIVITY_TAB, "0"))){
            bottomBar.selectTabAtPosition(1, false);
        }
        bottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                switch(menuItemId){
                    case R.id.record_item:  commitFragment(new StartRecordingFragment()); break;
                    case R.id.history_item: commitFragment(new SavedTracksFragment());break;
                    case R.id.profile_item: commitFragment(new UserPanelFragment());break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                switch(menuItemId){
                    case R.id.record_item: break;
                    case R.id.history_item: break; //scroll to top after reselecting
                    case R.id.profile_item: break;
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomBar.onSaveInstanceState(outState);
    }

    private void commitFragment(Fragment fragment){
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}
