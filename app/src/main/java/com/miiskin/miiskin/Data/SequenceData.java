package com.miiskin.miiskin.Data;

import android.database.Cursor;

import com.miiskin.miiskin.Storage.MiiskinDatabaseContract;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Newshka on 25.06.2015.
 */
public class SequenceData implements Serializable {
    public BodyPart mBodyPart;
    public float bodyPartRelativePointX;
    public float bodyPartRelativePointY;
    public Date mDateOfCreation;
    public String mId;

    public static SequenceData createFromCursor(Cursor cursor) {
        SequenceData sequenceData = new SequenceData();;
        sequenceData.mBodyPart = BodyPart.valueOf(cursor.getString(cursor.getColumnIndex(MiiskinDatabaseContract.MolePhotoSequence.COLUMN_NAME_ANATOMICAL_SECTION)));
        sequenceData.mDateOfCreation = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(MiiskinDatabaseContract.MolePhotoSequence.COLUMN_NAME_DATE_OF_CREATION_SEQUENCE))));
        sequenceData.bodyPartRelativePointX = Float.parseFloat(cursor.getString(cursor.getColumnIndex(MiiskinDatabaseContract.MolePhotoSequence.COLUMN_NAME_X_POSITION_OF_MOLE)));
        sequenceData.bodyPartRelativePointY = Float.parseFloat(cursor.getString(cursor.getColumnIndex(MiiskinDatabaseContract.MolePhotoSequence.COLUMN_NAME_Y_POSITION_OF_MOLE)));
        sequenceData.mId = cursor.getString(cursor.getColumnIndex(MiiskinDatabaseContract.MolePhotoSequence._ID));
        return sequenceData;
    }
}
