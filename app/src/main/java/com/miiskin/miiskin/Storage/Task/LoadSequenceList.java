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
        this(context, null);
    }

    public LoadSequenceList(Context context, Object params[]) {
        super(context, params);
    }

    @Override
    public String getTaskId() {
        return TASK_ID;
    }

    @Override
    public Object execute() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                MolePhotoSequence._ID,
                MolePhotoSequence.COLUMN_NAME_DATE_OF_CREATION_SEQUENCE,
                MolePhotoSequence.COLUMN_NAME_LAST_PHOTO_TIME,
                MolePhotoSequence.COLUMN_NAME_ANATOMICAL_SECTION,
                MolePhotoSequence.COLUMN_NAME_X_POSITION_OF_MOLE,
                MolePhotoSequence.COLUMN_NAME_Y_POSITION_OF_MOLE,
                MolePhotoSequence.COLUMN_NAME_USER_GENDER,
                MolePhotoSequence.COLUMN_NAME_USER_BIRTH_DATE
        };

        String sortOrder =
                MolePhotoSequence.COLUMN_NAME_DATE_OF_CREATION_SEQUENCE + " DESC";

        Cursor c = db.query(
                MolePhotoSequence.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        return c;
    }
}
