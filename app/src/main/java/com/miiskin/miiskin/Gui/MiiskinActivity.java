package com.miiskin.miiskin.Gui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.miiskin.miiskin.Data.AnalyticsNames;
import com.miiskin.miiskin.MiiskinApplication;
import com.miiskin.miiskin.Storage.Preferences;

import java.util.Date;

/**
 * Created by Newshka on 12.07.2015.
 */
public class MiiskinActivity extends AppCompatActivity {

    private Tracker mTracker;
    private long mStartTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MiiskinApplication application = (MiiskinApplication)getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStartTime = new Date().getTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long duration = new Date().getTime() - mStartTime;
        SharedPreferences settings = getSharedPreferences(Preferences.MAIN_PREFERENCES, 0);
        long totalTimeSpendInApp = settings.getLong(Preferences.UserInfo.TOTAL_TIME_SPEND_IN_APP, 0);
        totalTimeSpendInApp = totalTimeSpendInApp + duration;
        settings.edit().putLong(Preferences.UserInfo.TOTAL_TIME_SPEND_IN_APP, totalTimeSpendInApp).commit();

        long timeInAppInSeconds = totalTimeSpendInApp / 1000;
        mTracker.send(new HitBuilders.TimingBuilder()
                .setCategory(AnalyticsNames.TimingsCategory.USER_ENGAGEMENT)
                .setVariable(AnalyticsNames.TimingsNames.OPEN_TIME)
                .setValue(timeInAppInSeconds)
                .build());

    }
}
