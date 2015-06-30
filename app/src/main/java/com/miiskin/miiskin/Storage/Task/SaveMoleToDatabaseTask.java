package com.miiskin.miiskin.Storage.Task;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.miiskin.miiskin.Data.L;
import com.miiskin.miiskin.Data.MoleData;
import com.miiskin.miiskin.Data.UserManager;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract;

/**
 * Created by Newshka on 25.06.2015.
 */
public class SaveMoleToDatabaseTask extends Task {

    public SaveMoleToDatabaseTask(Context context) {
        this(context, null);
    }

    public SaveMoleToDatabaseTask(Context context, Object params[]) {
        super(context, params);
    }

    public static final String TASK_ID = "SaveCreatedSequenceToDatabase";

    @Override
    public Object execute() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        MoleData moleData = (MoleData)mParams[0];

        Long moleId = null;
        try {
            ContentValues values = new ContentValues();
            values.put(MiiskinDatabaseContract.MoleLocation.COLUMN_NAME_BODY_PART, moleData.mBodyPart.toString());
            values.put(MiiskinDatabaseContract.MoleLocation.COLUMN_NAME_X_POSITION_OF_MOLE, moleData.bodyPartRelativePointX);
            values.put(MiiskinDatabaseContract.MoleLocation.COLUMN_NAME_Y_POSITION_OF_MOLE, moleData.bodyPartRelativePointY);
            values.put(MiiskinDatabaseContract.MoleLocation.COLUMN_NAME_BODY_HALF, moleData.mBodyHalf.toString());

            long moleLocationId;
            moleLocationId = db.insert(
                    MiiskinDatabaseContract.MoleLocation.TABLE_NAME,
                    null,
                    values);

            values = new ContentValues();
            values.put(MiiskinDatabaseContract.Mole.COLUMN_NAME_START_OBSERVING_DATE, moleData.mDateOfCreation.getTime());
            values.put(MiiskinDatabaseContract.Mole.COLUMN_NAME_MOLE_LOCATION_ID, moleLocationId);
            values.put(MiiskinDatabaseContract.Mole.COLUMN_NAME_USER_ID, UserManager.getInstance().getUserId());

            moleId = db.insert(
                    MiiskinDatabaseContract.Mole.TABLE_NAME,
                    null,
                    values);

        } catch (Exception exception ) {
            L.e("Can't insert mole to database: ", exception);
        }
        return moleId;
    }

    @Override
    public String getTaskId() {
        return TASK_ID;
    }
}
