package com.miiskin.miiskin.Storage.Task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MolePhotoSequence;
import com.miiskin.miiskin.Storage.MiiskinDbHelper;

/**
 * Created by Newshka on 24.06.2015.
 */
public class LoadSequenceList extends Task{

    public static String TASK_ID = "LoadSequenceList";

    public LoadSequenceList(Context context) {
        super(context);
    }




    @Override
    public Cursor execute() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                MolePhotoSequence._ID,
                MolePhotoSequence.COLUMN_NAME_DATE_OF_CREATION_SEQUENCE,
                MolePhotoSequence.COLUMN_NAME_LAST_PHOTO_TIME,
                MolePhotoSequence.COLUMN_NAME_ANATOMICAL_SECTION,
                MolePhotoSequence.COLUMN_NAME_POSITION_OF_MOLE,
                MolePhotoSequence.COLUMN_NAME_USER_GENDER,
                MolePhotoSequence.COLUMN_NAME_USER_BIRTH_DATE
        };

        String sortOrder =
                MolePhotoSequence.COLUMN_NAME_DATE_OF_CREATION_SEQUENCE + " DESC";

        Cursor c = db.query(
                MolePhotoSequence.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return c;
    }
}
