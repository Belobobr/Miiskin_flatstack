package com.miiskin.miiskin;

import android.app.Application;
import android.content.Context;

/**
 * Created by Newshka on 26.06.2015.
 */
public class MiiskinApplication extends Application {

        private static Context mContext;

        @Override
        public void onCreate() {
            super.onCreate();
            MiiskinApplication.mContext = getApplicationContext();
        }


    public static Context getAppContext() {
        return MiiskinApplication.mContext;
    }

}
