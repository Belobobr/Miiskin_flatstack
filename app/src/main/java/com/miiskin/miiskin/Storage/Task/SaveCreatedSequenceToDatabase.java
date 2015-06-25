package com.miiskin.miiskin.Storage.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.miiskin.miiskin.Data.SequenceData;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MolePhotoSequence;

/**
 * Created by Newshka on 25.06.2015.
 */
public class SaveCreatedSequenceToDatabase extends Task {

    public SaveCreatedSequenceToDatabase(Context context) {
        this(context, null);
    }

    public SaveCreatedSequenceToDatabase(Context context, Object params[]) {
        super(context, params);
    }

    public static final String TASK_ID = "SaveCreatedSequenceToDatabase";

    @Override
    public Object execute() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        SequenceData sequenceData = (SequenceData)mParams[0];

        ContentValues values = new ContentValues();
        values.put(MolePhotoSequence.COLUMN_NAME_ANATOMICAL_SECTION, sequenceData.mBodyPart.toString());
        values.put(MolePhotoSequence.COLUMN_NAME_X_POSITION_OF_MOLE, sequenceData.bodyPartRelativePointX);
        values.put(MolePhotoSequence.COLUMN_NAME_Y_POSITION_OF_MOLE, sequenceData.bodyPartRelativePointY);
        values.put(MolePhotoSequence.COLUMN_NAME_DATE_OF_CREATION_SEQUENCE, sequenceData.mDateOfCreation.getTime());

        long newRowId;
        newRowId = db.insert(
                MolePhotoSequence.TABLE_NAME,
                null,
                values);
        return newRowId;
    }

    @Override
    public String getTaskId() {
        return TASK_ID;
    }
}
