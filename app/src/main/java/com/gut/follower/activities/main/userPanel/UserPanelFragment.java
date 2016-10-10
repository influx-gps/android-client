package com.gut.follower.activities.main.userPanel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gut.follower.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserPanelFragment extends Fragment implements UserPanelContract.View{

    private UserPanelContract.Presenter mPresenter;
    private Button mLogoutBtn;

    public UserPanelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_panel, container, false);

        mPresenter = new UserPanelPresenter();
        mPresenter.attachView(this);
        mLogoutBtn = (Button)view.findViewById(R.id.logout_btn);
        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.logout();
            }
        });

        return view;
    }

}
