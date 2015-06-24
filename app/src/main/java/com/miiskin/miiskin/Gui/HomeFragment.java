package com.miiskin.miiskin.Gui;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;

import com.miiskin.miiskin.Storage.Task.TaskManager;

/**
 * Created by Newshka on 23.06.2015.
 */
public class HomeFragment extends Fragment implements TaskManager.DataChangeListener {

    Cursor moleSequenceListCursor;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        TaskManager.getInstance(getActivity().getApplicationContext()).addDataChangeListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        TaskManager.getInstance(getActivity().getApplicationContext()).removeDataChangeListener(this);
        super.onPause();
    }

    @Override
    public void onDataChanged(String dataId) {
        
    }
}
