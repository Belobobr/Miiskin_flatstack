package com.miiskin.miiskin;

import android.content.Context;

import com.miiskin.miiskin.Storage.MiiskinDbHelper;
import com.miiskin.miiskin.Storage.Preferences;

/**
 * Created by Newshka on 12.07.2015.
 */
public class TestHelper {

    /**
     * @param context app context
     */
    public static final void clearData(Context context) {
        //clear preferences
        context.getSharedPreferences(Preferences.MAIN_PREFERENCES, Context.MODE_PRIVATE).edit().clear().commit();
        //clear database
        context.deleteDatabase(MiiskinDbHelper.DATABASE_NAME);
    }
}
