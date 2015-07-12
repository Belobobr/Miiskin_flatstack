package com.miiskin.miiskin;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Newshka on 26.06.2015.
 */
public class MiiskinApplication extends Application {

    private static Context mContext;
    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.setLocalDispatchPeriod(1800);

            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker("UA-64825827-1");
            mTracker.enableExceptionReporting(true);
            mTracker.enableAdvertisingIdCollection(true);
        }
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MiiskinApplication.mContext = getApplicationContext();
    }


    public static Context getAppContext() {
        return MiiskinApplication.mContext;
    }

}
