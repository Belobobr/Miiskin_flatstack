package com.miiskin.miiskin.Storage;

import java.util.Calendar;

/**
 * Created by Newshka on 23.06.2015.
 */
public class Preferences {

    public static String MAIN_PREFERENCES = "MAIN_PREFERENCES";
    public static String USER_INFO = "USER_INFO";

    public static class FirstTimeUse {
        //FTE - first time experience
        public static String FTE = "FTE";

        public static String FTE_SHOW_DISCLAIMER = "FTE_SHOW_DISCLAIMER";

        public static String FTE_SHOW_CAMERA_TIPS = "FTE_SHOW_CAMERA_TIPS";
    }

    public static class UserInfo {
        public static final String CURRENT_USER_ID = "CURRENT_USER_ID";
        public static final String CURRENT_USER_GENDER = "CURRENT_USER_GENDER";
        public static final String TOTAL_TIME_SPEND_IN_APP = "TOTAL_TIME_SPEND_IN_APP";

        public static int DATE_OF_BIRTH_FROM = 1965;
        public static int DATE_OF_BIRTH_TO = Calendar.getInstance().get(Calendar.YEAR);
    }
}
