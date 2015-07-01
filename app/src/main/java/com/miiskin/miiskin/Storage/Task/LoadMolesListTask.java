package com.miiskin.miiskin.Storage.Task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.miiskin.miiskin.Data.UserManager;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.Mole;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MoleLocation;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MolePicture;

/**
 * Created by Newshka on 24.06.2015.
 */
public class LoadMolesListTask extends Task{

    public static String TASK_ID = "LoadSequenceList";

    public LoadMolesListTask(Context context) {
        this(context, null);
    }

    public LoadMolesListTask(Context context, Object params[]) {
        super(context, params);
    }

    @Override
    public String getTaskId() {
        return TASK_ID;
    }

    private static final String COMMA_SEP = ", ";

    public static final String LAST_PICTURE_TIME = "last_picture_time";

    private static final String MOLE_FOR_USER_SELECT_QUERY = "SELECT * FROM " + Mole.TABLE_NAME +
            " INNER JOIN " + MoleLocation.TABLE_NAME + " ON " + Mole.TABLE_NAME + "." + Mole.COLUMN_NAME_MOLE_LOCATION_ID
            + " = " + MoleLocation.TABLE_NAME + "." + MoleLocation._ID + " WHERE " + Mole.COLUMN_NAME_USER_ID + " = ?";

    private static final String MOLE_FOR_USER_SELECT_QUERY_LAST_PHOTO = "SELECT " + Mole.TABLE_NAME +"." + Mole._ID + COMMA_SEP + Mole.COLUMN_NAME_START_OBSERVING_DATE + COMMA_SEP + Mole.COLUMN_NAME_USER_ID + COMMA_SEP +
            Mole.COLUMN_NAME_MOLE_LOCATION_ID + COMMA_SEP + MoleLocation.COLUMN_NAME_Y_POSITION_OF_MOLE + COMMA_SEP + MoleLocation.COLUMN_NAME_X_POSITION_OF_MOLE +
            COMMA_SEP + MoleLocation.COLUMN_NAME_BODY_HALF + COMMA_SEP + MoleLocation.COLUMN_NAME_BODY_PART + COMMA_SEP + "COALESCE( (SELECT MAX(" + MolePicture.COLUMN_NAME_TIME + ") FROM " +
            MolePicture.TABLE_NAME + " WHERE " + Mole.TABLE_NAME + "."  + Mole._ID + " = " + MolePicture.TABLE_NAME + "." + MolePicture.COLUMN_NAME_MOLE_ID + "), -1)" + " AS " + LAST_PICTURE_TIME +
            " FROM " + Mole.TABLE_NAME +
            " INNER JOIN " + MoleLocation.TABLE_NAME + " ON " + Mole.TABLE_NAME + "." + Mole.COLUMN_NAME_MOLE_LOCATION_ID
            + " = " + MoleLocation.TABLE_NAME + "." + MoleLocation._ID + " WHERE " + Mole.TABLE_NAME + "." + Mole.COLUMN_NAME_USER_ID + " = ? " +
            "ORDER BY " + Mole.COLUMN_NAME_START_OBSERVING_DATE ;

    @Override
    public Object execute() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(MOLE_FOR_USER_SELECT_QUERY_LAST_PHOTO, new String[]{String.valueOf(UserManager.getInstance().getUserId())}
        );
        return c;
    }
}
