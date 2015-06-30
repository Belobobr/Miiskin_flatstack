package com.miiskin.miiskin.Storage.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.miiskin.miiskin.Data.UserInfo;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.User;

/**
 * Created by Newshka on 30.06.2015.
 */
public class SaveUserInfo extends Task {

    public SaveUserInfo(Context context) {
        this(context, null);
    }

    public SaveUserInfo(Context context, Object params[]) {
        super(context, params);
    }

    public static final String TASK_ID = "SaveUserInfo";

    @Override
    public Object execute() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        UserInfo userInfo = (UserInfo)mParams[0];

        ContentValues values = new ContentValues();
        values.put(User.COLUMN_NAME_BIRTH_DATE, userInfo.birth_date);
        values.put(User.COLUMN_NAME_GENDER, userInfo.gender);
        values.put(User.COLUMN_NAME_EMAIL, userInfo.email);

        long newRowId;
        newRowId = db.insert(
                User.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    @Override
    public String getTaskId() {
        return TASK_ID;
    }
}
