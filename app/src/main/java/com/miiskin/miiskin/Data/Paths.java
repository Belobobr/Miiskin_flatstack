package com.miiskin.miiskin.Data;

import android.os.Environment;

import java.io.File;

/**
 * Created by Newshka on 26.06.2015.
 */
public class Paths {

    public static final String APPLICATION_DIR_NAME = "miiskin";

    public static String getRelativeDirForSequence(String sequenceId) {
        return APPLICATION_DIR_NAME + "/" + sequenceId;
    }

    public static String getAbsoluteDirForSequence(String sequenceId) {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);

        File sequencePhotoDir = new File(path, Paths.getRelativeDirForSequence(sequenceId));

        return sequencePhotoDir.toString();
    }

    public static String getAbsolutePathForFileInSequence(String sequenceId, int imageId) {
        return Paths.getAbsoluteDirForSequence(sequenceId) + "/" + imageId + ".png";
    }
}
