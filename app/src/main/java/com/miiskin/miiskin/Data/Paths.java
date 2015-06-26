package com.miiskin.miiskin.Data;

/**
 * Created by Newshka on 26.06.2015.
 */
public class Paths {

    public static final String APPLICATION_DIR_NAME = "miiskin";

    public static String getRelativeDirForSequence(String sequenceId) {
        return APPLICATION_DIR_NAME + "/" + sequenceId;
    }
}
