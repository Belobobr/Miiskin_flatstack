package com.miiskin.miiskin.Storage.Task;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Newshka on 24.06.2015.
 */
public class TaskManager {
    private static volatile TaskManager instance;
    private TaskManager() { }

    public Map<String, WeakReference<Object>> cachedCursors = new HashMap<>();
    public Context mContext;
    private List<DataChangeListener> mDataChangeListeners = new ArrayList<DataChangeListener>();

    public static TaskManager getInstance(Context context) {
        if (instance == null ) {
            synchronized (TaskManager.class) {
                if (instance == null) {
                    instance = new TaskManager();
                }
            }
        }

        instance.mContext = context;
        return instance;
    }

    public void addDataChangeListener(DataChangeListener dataChangeListener) {
        mDataChangeListeners.add(dataChangeListener);
    }

    public void removeDataChangeListener(DataChangeListener dataChangeListener) {
        mDataChangeListeners.remove(dataChangeListener);
    }

    public void notifyDataChangeListener(String dataId) {
        for (DataChangeListener dataChangeListener : mDataChangeListeners) {
            dataChangeListener.onDataChanged(dataId);
        }
    }

    public Object getDataById(String taskId) {
        WeakReference<Object> weakReference = cachedCursors.get(taskId);
        if (weakReference != null) {
            return weakReference.get();
        } else {
            return null;
        }
    }

    public void executeTask(Task task, final String taskId) {
        this.executeTask(task, taskId, null);
    }

    public void executeTask(Task task, final String taskId, Object params[]) {

        if (taskId.equals(LoadSequenceList.TASK_ID)) {
            task = new LoadSequenceList(mContext, params);
        } else if (taskId.equals(SaveCreatedSequenceToDatabase.TASK_ID)) {
            task = new SaveCreatedSequenceToDatabase(mContext, params);
        }

        if (task != null) {
            AsyncTask<Task, Void, Object> asyncTask = new AsyncTask<Task, Void, Object>() {
                @Override
                protected Object doInBackground(Task... params) {
                    return params[0].execute();
                }

                @Override
                protected void onPostExecute(Object object) {
                    cachedCursors.put(taskId, new WeakReference<Object>(object));
                    notifyDataChangeListener(taskId);
                }
            };
            asyncTask.execute(task);
        }
    }

    public interface DataChangeListener {
        public void onDataChanged(String dataId);
    }

}
