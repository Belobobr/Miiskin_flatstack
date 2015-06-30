package com.miiskin.miiskin.Data;

import android.database.Cursor;

import com.miiskin.miiskin.Storage.MiiskinDatabaseContract;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Newshka on 25.06.2015.
 */
public class MoleData implements Serializable {
    public BodyPart mBodyPart;
    public float bodyPartRelativePointX;
    public float bodyPartRelativePointY;
    public Date mDateOfCreation;
    public BodyHalf mBodyHalf;
    public String mId;

    public static MoleData createFromCursor(Cursor cursor) {
        MoleData moleData = new MoleData();
        moleData.mBodyPart = BodyPart.valueOf(cursor.getString(cursor.getColumnIndex(MiiskinDatabaseContract.MoleLocation.COLUMN_NAME_BODY_PART)));
        moleData.mDateOfCreation = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(MiiskinDatabaseContract.Mole.COLUMN_NAME_START_OBSERVING_DATE))));
        moleData.bodyPartRelativePointX = Float.parseFloat(cursor.getString(cursor.getColumnIndex(MiiskinDatabaseContract.MoleLocation.COLUMN_NAME_X_POSITION_OF_MOLE)));
        moleData.bodyPartRelativePointY = Float.parseFloat(cursor.getString(cursor.getColumnIndex(MiiskinDatabaseContract.MoleLocation.COLUMN_NAME_Y_POSITION_OF_MOLE)));
        moleData.mBodyHalf = BodyHalf.valueOf(cursor.getString(cursor.getColumnIndex(MiiskinDatabaseContract.MoleLocation.COLUMN_NAME_BODY_HALF)));
        moleData.mId = cursor.getString(cursor.getColumnIndex(MiiskinDatabaseContract.Mole._ID));
        return moleData;
    }
}
