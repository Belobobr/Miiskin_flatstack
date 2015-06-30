package com.miiskin.miiskin.Storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.User;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.Mole;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MoleLocation;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MolePicture;

/**
 * Created by Newshka on 24.06.2015.
 */
public class MiiskinDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Miiskin.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + User.TABLE_NAME + " (" +
                    User._ID + " INTEGER PRIMARY KEY," +
                    User.COLUMN_NAME_BIRTH_DATE + TEXT_TYPE + COMMA_SEP +
                    User.COLUMN_NAME_GENDER + TEXT_TYPE + COMMA_SEP +
                    User.COLUMN_NAME_EMAIL + TEXT_TYPE +
            " )";

    private static final String SQL_CREATE_MOLE_TABLE =
            "CREATE TABLE " + Mole.TABLE_NAME + " (" +
                    Mole._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    Mole.COLUMN_NAME_MOLE_LOCATION_ID + " INTEGER" + COMMA_SEP +
                    Mole.COLUMN_NAME_USER_ID + " INTEGER" + COMMA_SEP +
                    Mole.COLUMN_NAME_START_OBSERVING_DATE + TEXT_TYPE + COMMA_SEP +
                    "FOREIGN KEY" + "(" + Mole.COLUMN_NAME_MOLE_LOCATION_ID + ")" + " REFERENCES " + MoleLocation.TABLE_NAME + "(" + MoleLocation._ID + ")" + "ON DELETE CASCADE"  + COMMA_SEP +
                    "FOREIGN KEY" + "(" + Mole.COLUMN_NAME_USER_ID + ")" + " REFERENCES " + User.TABLE_NAME + "(" + User._ID + ")" + "ON DELETE CASCADE" +
                    " )";

    private static final String SQL_CREATE_MOLE_PICTURE_TABLE=
            "CREATE TABLE " + MolePicture.TABLE_NAME + " (" +
                    MolePicture._ID + " INTEGER PRIMARY KEY " + COMMA_SEP +
                    MolePicture.COLUMN_NAME_IMAGE_PATH + TEXT_TYPE + COMMA_SEP +
                    MolePicture.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    MolePicture.COLUMN_NAME_MOLE_ID + TEXT_TYPE + COMMA_SEP +
                    "FOREIGN KEY" + "(" + MolePicture.COLUMN_NAME_MOLE_ID + ")" + " REFERENCES " + Mole.TABLE_NAME + "(" + Mole._ID + ")" + "ON DELETE CASCADE" +
                    " )";

    private static final String SQL_CREATE_MOLE_LOCATION_TABLE=
            "CREATE TABLE " + MoleLocation.TABLE_NAME + " (" +
                    MoleLocation._ID + " INTEGER PRIMARY KEY " + COMMA_SEP +
                    MoleLocation.COLUMN_NAME_BODY_PART + TEXT_TYPE + COMMA_SEP +
                    MoleLocation.COLUMN_NAME_BODY_HALF+ TEXT_TYPE + COMMA_SEP +
                    MoleLocation.COLUMN_NAME_X_POSITION_OF_MOLE + TEXT_TYPE + COMMA_SEP +
                    MoleLocation.COLUMN_NAME_Y_POSITION_OF_MOLE+ TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_USER_TABLE =
            "DROP TABLE IF EXISTS " + User.TABLE_NAME;

    private static final String SQL_DELETE_MOLE_TABLE =
            "DROP TABLE IF EXISTS " + Mole.TABLE_NAME;

    private static final String SQL_DELETE_MOLE_PICTURE_TABLE =
            "DROP TABLE IF EXISTS " + MolePicture.TABLE_NAME;

    private static final String SQL_DELETE_MOLE_LOCATION_TABLE =
            "DROP TABLE IF EXISTS " + MoleLocation.TABLE_NAME;


    public MiiskinDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_TABLE);
        db.execSQL(SQL_CREATE_MOLE_TABLE);
        db.execSQL(SQL_CREATE_MOLE_PICTURE_TABLE);
        db.execSQL(SQL_CREATE_MOLE_LOCATION_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_USER_TABLE);
        db.execSQL(SQL_DELETE_MOLE_TABLE);
        db.execSQL(SQL_DELETE_MOLE_PICTURE_TABLE);
        db.execSQL(SQL_DELETE_MOLE_LOCATION_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
