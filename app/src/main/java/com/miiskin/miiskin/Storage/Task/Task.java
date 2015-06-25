package com.miiskin.miiskin.Storage.Task;

import android.content.Context;
import android.database.Cursor;

import com.miiskin.miiskin.Storage.MiiskinDbHelper;

/**
 * Created by Newshka on 24.06.2015.
 */
public abstract class Task {

    protected Context mContext;
    protected Object mParams;
    protected MiiskinDbHelper mDbHelper;
    abstract public Cursor execute();
    abstract public String getTaskId();

    protected Task(Context context) {
        mContext = context;
        mDbHelper  = new MiiskinDbHelper(context);
    }

    protected Task (Context context, Object params[]) {
        this(context);
        mParams = params;
    }


}
