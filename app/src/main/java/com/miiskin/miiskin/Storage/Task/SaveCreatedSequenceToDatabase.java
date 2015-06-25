package com.miiskin.miiskin.Storage.Task;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by Newshka on 25.06.2015.
 */
public class SaveCreatedSequenceToDatabase extends Task {

    public SaveCreatedSequenceToDatabase(Context context) {
        this(context, null);
    }

    public SaveCreatedSequenceToDatabase(Context context, Object params[]) {
        super(context, params);
    }

    public static final String TASK_ID = "SaveCreatedSequenceToDatabase";

    @Override
    public Cursor execute() {
        return null;
    }

    @Override
    public String getTaskId() {
        return null;
    }
}
