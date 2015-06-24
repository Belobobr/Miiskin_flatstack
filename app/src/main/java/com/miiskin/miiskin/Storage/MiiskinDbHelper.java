package com.miiskin.miiskin.Storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MolePhotoSequence;

/**
 * Created by Newshka on 24.06.2015.
 */
public class MiiskinDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Miiskin.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MolePhotoSequence.TABLE_NAME + " (" +
                    MolePhotoSequence._ID + " INTEGER PRIMARY KEY," +
                    MolePhotoSequence.COLUMN_NAME_ANATOMICAL_SECTION + TEXT_TYPE + COMMA_SEP +
                    MolePhotoSequence.COLUMN_NAME_DATE_OF_CREATION_SEQUENCE + TEXT_TYPE + COMMA_SEP +
                    MolePhotoSequence.COLUMN_NAME_LAST_PHOTO_TIME + TEXT_TYPE + COMMA_SEP +
                    MolePhotoSequence.COLUMN_NAME_POSITION_OF_MOLE + TEXT_TYPE + COMMA_SEP +
                    MolePhotoSequence.COLUMN_NAME_USER_BIRTH_DATE + TEXT_TYPE + COMMA_SEP +
                    MolePhotoSequence.COLUMN_NAME_USER_GENDER + TEXT_TYPE + COMMA_SEP +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MolePhotoSequence.TABLE_NAME;

    public MiiskinDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
