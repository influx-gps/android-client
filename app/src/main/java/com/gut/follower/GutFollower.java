package com.gut.follower;

import android.app.Application;
import android.content.Context;

public class GutFollower extends Application{

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

    }
}
