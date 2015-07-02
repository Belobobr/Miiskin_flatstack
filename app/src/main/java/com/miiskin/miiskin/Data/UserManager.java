package com.miiskin.miiskin.Data;

import android.content.SharedPreferences;

import com.miiskin.miiskin.MiiskinApplication;
import com.miiskin.miiskin.Storage.Preferences;

/**
 * Created by Newshka on 30.06.2015.
 */
public class UserManager {

    private static UserManager instance;

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private Long mUserId;

    private UserManager() {

    }

    public void setUserInfo(UserInfo userInfo) {
        SharedPreferences settings = MiiskinApplication.getAppContext().getSharedPreferences(Preferences.USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(Preferences.UserInfo.CURRENT_USER_ID, userInfo.userId);
        editor.putString(Preferences.UserInfo.CURRENT_USER_GENDER, userInfo.gender);
        editor.commit();
        mUserId = userInfo.userId;
    }

    public long getUserId() {
        if (mUserId != null) {
            return mUserId;
        } else {
            SharedPreferences settings = MiiskinApplication.getAppContext().getSharedPreferences(Preferences.USER_INFO, 0);
            mUserId = settings.getLong(Preferences.UserInfo.CURRENT_USER_ID, -1);
            return mUserId;
        }
    }

    public String getUserGender() {
        SharedPreferences settings = MiiskinApplication.getAppContext().getSharedPreferences(Preferences.USER_INFO, 0);
        String gender = settings.getString(Preferences.UserInfo.CURRENT_USER_GENDER, UserInfo.MALE);
        return gender;
    }
}
