package com.miiskin.miiskin;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;

import com.miiskin.miiskin.Data.BodyHalf;
import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Data.MoleData;
import com.miiskin.miiskin.Data.UserInfo;
import com.miiskin.miiskin.Data.UserManager;
import com.miiskin.miiskin.Storage.MiiskinDbHelper;
import com.miiskin.miiskin.Storage.Preferences;
import com.miiskin.miiskin.Storage.Task.SaveMoleToDatabaseTask;
import com.miiskin.miiskin.Storage.Task.SaveUserInfoTask;

import java.util.Date;

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


    public static void initFirstSequence(Instrumentation instrumentation) {
        initializePreferences(instrumentation);
        initializeUser(instrumentation);
        initializeDatabase(instrumentation);
    }

    private static void initializeUser(Instrumentation instrumentation) {
        UserInfo userInfo = new UserInfo();
        userInfo.gender = UserInfo.MALE;
        userInfo.birth_date = "1965";
        SaveUserInfoTask saveUserInfoTask = new SaveUserInfoTask(instrumentation.getTargetContext(), new Object[]{userInfo});
        userInfo = (UserInfo)saveUserInfoTask.execute();
        UserManager.getInstance().setUserInfo(userInfo);
    }

    private static void initializePreferences(Instrumentation instrumentation) {
        setFte(instrumentation, false);
    }

    private static void setFte(Instrumentation instrumentation, boolean fte) {
        SharedPreferences settings = instrumentation.getTargetContext().getSharedPreferences(Preferences.MAIN_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Preferences.FirstTimeUse.FTE, fte);
        editor.commit();
    }

    private static void initializeDatabase(Instrumentation instrumentation) {
        Context context = instrumentation.getTargetContext();
        MoleData moleData = new MoleData();
        moleData.mBodyPart = BodyPart.Chest;
        moleData.mBodyHalf = BodyHalf.Front;
        moleData.bodyPartRelativePointX = 0.5f;
        moleData.bodyPartRelativePointY = 0.5f;
        moleData.mDateOfCreation = new Date();
        SaveMoleToDatabaseTask saveMoleToDatabaseTask = new SaveMoleToDatabaseTask(context, new Object[] {moleData});
        Long moleId = (Long)saveMoleToDatabaseTask.execute();
        moleData.mId = String.valueOf(moleId);
    }
}
