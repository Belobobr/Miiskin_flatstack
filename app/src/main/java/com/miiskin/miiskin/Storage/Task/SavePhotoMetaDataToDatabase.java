package com.miiskin.miiskin.Storage.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.analytics.HitBuilders;
import com.miiskin.miiskin.Data.AnalyticsNames;
import com.miiskin.miiskin.Data.L;
import com.miiskin.miiskin.Data.MoleData;
import com.miiskin.miiskin.Data.SavedPhotoInfo;
import com.miiskin.miiskin.MiiskinApplication;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract;

import java.util.Date;

/**
 * Created by Newshka on 01.07.2015.
 */
public class SavePhotoMetaDataToDatabase  extends Task{

    SavedPhotoInfo mSavedPhotoInfo;
    MoleData mMoleData;

    public SavePhotoMetaDataToDatabase(Context context) {
        this(context, null);
    }

    public SavePhotoMetaDataToDatabase(Context context, Object params[]) {
        super(context, params);
    }

    public static final String TASK_ID = "SavePhotoMetaDataToDatabase";

    @Override
    public Object execute() {
        mSavedPhotoInfo = (SavedPhotoInfo)mParams[0];
        mMoleData = (MoleData)mParams[1];

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MiiskinDatabaseContract.MolePicture.COLUMN_NAME_IMAGE_PATH, mSavedPhotoInfo.mPath);
        contentValues.put(MiiskinDatabaseContract.MolePicture.COLUMN_NAME_TIME, new Date().getTime());
        contentValues.put(MiiskinDatabaseContract.MolePicture.COLUMN_NAME_MOLE_ID, Long.parseLong(mMoleData.mId));

        long imageId = db.insert(MiiskinDatabaseContract.MolePicture.TABLE_NAME, null, contentValues);
        if (imageId == -1) {
            L.e("Can't save photo to database");
        }
        return imageId;
    }

    @Override
    public String getTaskId() {
        return TASK_ID;
    }
}
