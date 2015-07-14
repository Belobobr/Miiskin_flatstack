package com.miiskin.miiskin.Data;

/**
 * Created by Newshka on 08.07.2015.
 */
public class AnalyticsNames {

    public static final String HOME_ACTIVITY_SCREEN_NAME = "Home screen";
    public static final String HOME_ACTIVITY_FTE_SCREEN_NAME = "Home screen fte";
    public static final String CREATE_MOLE_GENERAL_AREA = "Create mole general area screen";
    public static final String CREATE_MOLE_SPECIFIC_AREA = "Create mole specific area screen";
    public static final String VIEW_MOLE = "View mole screen";
    public static final String SEND_TO_DOCTOR = "Send to doctor screen";
    public static final String CAMERA_SCREEN = "Camera screen";
    public static final String APPROVE_PHOTO_SCREEN = "Approve photo screen";

    public static class EventCategory {
        public static final String INFORMATION = "Information";
        public static final String ASSESSMENT = "Assessment";
        public static final String SEQUENCES = "Sequences";
        public static final String PICTURES = "Pictures";
    }

    public static class EventAction {
        public static final String ABOUT_DOCTOR_ACTION = "About our doctor";
        public static final String DERMATOLOGICAL_ASSESSMENT = "Dermatological assessment";
        public static final String PAY_AND_SEND_PHOTO = "Pay and send photo";
        public static final String SEQUENCE_CREATED = "Sequence created";
        public static final String PICTURES_TAKEN = "Pictures taken";
    }

    public static class TimingsCategory {
        public static final String USER_ENGAGEMENT = "User Engagement";
    }

    public static class TimingsNames {
        public static final String OPEN_TIME = "Total Open Time";
    }

    public static class CustomDimension {
        public static final int SEQUENCES_CREATED_ID = 1;
        public static final String SEQUENCES_CREATED = "SEQUENCES_CREATED";
        public static final int PICTURES_TAKEN_ID = 2;
        public static final String PICTURES_TAKEN = "PICTURES TAKEN";
    }

    public static class CustomMetrics {
        public static final int SEQUENCES_CREATED_ID = 1;
        public static final String SEQUENCES_CREATED = "SEQUENCES_CREATED";
        public static final int PICTURES_TAKEN_ID = 2;
        public static final String PICTURES_TAKEN = "PICTURES TAKEN";
        public static final int OPEN_TIME_ID = 3;
        public static final String OPEN_TIME = "OPEN_TIME";
    }
}
