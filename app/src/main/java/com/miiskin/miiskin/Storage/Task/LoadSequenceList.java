package com.miiskin.miiskin.Storage.Task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miiskin.miiskin.Data.UserManager;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.Mole;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MoleLocation;

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

    private static final String MOLE_FOR_USER_SELECT_QUERY = "SELECT * FROM " + Mole.TABLE_NAME +
            " INNER JOIN " + MoleLocation.TABLE_NAME + " ON " + Mole.TABLE_NAME + "." + Mole.COLUMN_NAME_MOLE_LOCATION_ID
            + " = " + MoleLocation.TABLE_NAME + "." + MoleLocation._ID + " WHERE " + Mole.COLUMN_NAME_USER_ID + " = ?";

    @Override
    public Object execute() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(MOLE_FOR_USER_SELECT_QUERY, new String[]{String.valueOf(UserManager.getInstance().getUserId())}
        );
        return c;
    }
}
