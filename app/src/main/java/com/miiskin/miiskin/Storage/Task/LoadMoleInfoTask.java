package com.miiskin.miiskin.Storage.Task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miiskin.miiskin.Storage.MiiskinDatabaseContract;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.Mole;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MolePicture;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MoleLocation;

/**
 * Created by Newshka on 30.06.2015.
 */
public class LoadMoleInfoTask extends Task {

    public static class Result {
        public Result(Cursor picturesCursor, Cursor generalInfoCursor) {
            mPicturesCursor = picturesCursor;
            mGeneralInfoCursor = generalInfoCursor;
        }

        public Cursor mPicturesCursor;
        public Cursor mGeneralInfoCursor;
    }

    public static String TASK_ID = "LoadMoleInfoTask";

    public LoadMoleInfoTask(Context context) {
        this(context, null);
    }

    public LoadMoleInfoTask(Context context, Object params[]) {
        super(context, params);
    }

    @Override
    public String getTaskId() {
        return TASK_ID;
    }

    private static final String MOLE_PICTURES_QUERY = "SELECT * FROM " + Mole.TABLE_NAME +
            " INNER JOIN " + MolePicture.TABLE_NAME + " ON " + Mole.TABLE_NAME + "." + Mole._ID +
            " = " + MolePicture.TABLE_NAME + "." + MolePicture.COLUMN_NAME_MOLE_ID +
            " WHERE " + Mole.TABLE_NAME + "." + Mole._ID + " = ?";


    private static final String MOLE_GENERAL_INFO_QUERY = "SELECT * FROM " + Mole.TABLE_NAME +
            " INNER JOIN " + MoleLocation.TABLE_NAME + " ON " + Mole.TABLE_NAME + "." + Mole.COLUMN_NAME_MOLE_LOCATION_ID
            + " = " + MoleLocation.TABLE_NAME + "." + MoleLocation._ID +
            " WHERE " + Mole.TABLE_NAME + "." + Mole._ID + " = ?";


    @Override
    public Object execute() {
        Long moleId = (Long)mParams[0];

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor picturesCursor = db.rawQuery(MOLE_PICTURES_QUERY, new String[]{String.valueOf(moleId)});
        Cursor generalInfoCursor = db.rawQuery(MOLE_GENERAL_INFO_QUERY, new String[]{String.valueOf(moleId)});
        Result result = new Result(picturesCursor, generalInfoCursor);
        return result;
    }
}
