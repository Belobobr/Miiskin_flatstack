package com.miiskin.miiskin.Storage;

import android.provider.BaseColumns;

/**
 * Created by Newshka on 24.06.2015.
 */
public class MiiskinDatabaseContract {

    public MiiskinDatabaseContract() {}


    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "User";
        public static final String COLUMN_NAME_BIRTH_DATE = "birth_date";
        public static final String COLUMN_NAME_GENDER = "gender";
        public static final String COLUMN_NAME_EMAIL = "email";
    }

    public static abstract class Mole implements BaseColumns {
        public static final String TABLE_NAME = "Mole";
        public static final String COLUMN_NAME_MOLE_LOCATION_ID = "mole_location_id";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_START_OBSERVING_DATE = "start_observing_date";
    }

    public static abstract class MolePicture  implements BaseColumns {
        public static final String TABLE_NAME = "MolePicture";
        public static final String COLUMN_NAME_IMAGE_PATH = "image_pat";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_MOLE_ID = "mole_id";
    }

    public static abstract class MoleLocation implements BaseColumns {
        public static final String TABLE_NAME = "MoleLocation";
        public static final String COLUMN_NAME_BODY_PART = "body_part";
        public static final String COLUMN_NAME_BODY_HALF = "body_half";
        public static final String COLUMN_NAME_X_POSITION_OF_MOLE = "position_of_mole_x";
        public static final String COLUMN_NAME_Y_POSITION_OF_MOLE = "position_of_mole_y";
    }


}
