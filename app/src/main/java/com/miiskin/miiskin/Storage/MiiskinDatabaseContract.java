package com.miiskin.miiskin.Storage;

import android.provider.BaseColumns;

/**
 * Created by Newshka on 24.06.2015.
 */
public class MiiskinDatabaseContract {

    public MiiskinDatabaseContract() {}

    public static abstract class MolePhotoSequence implements BaseColumns {
        public static final String TABLE_NAME = "sequence";
        public static final String COLUMN_NAME_ANATOMICAL_SECTION = "anatomical_section";
        public static final String COLUMN_NAME_X_POSITION_OF_MOLE = "position_of_mole_x";
        public static final String COLUMN_NAME_Y_POSITION_OF_MOLE = "position_of_mole_y";
        public static final String COLUMN_NAME_LAST_PHOTO_TIME = "last_photo_time";
        public static final String COLUMN_NAME_DATE_OF_CREATION_SEQUENCE = "date_creation";
        public static final String COLUMN_NAME_USER_BIRTH_DATE = "birth_date_user";
        public static final String COLUMN_NAME_USER_GENDER = "user_gender";
    }
}
