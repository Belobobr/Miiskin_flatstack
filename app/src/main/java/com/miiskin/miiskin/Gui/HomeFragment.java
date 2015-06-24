package com.miiskin.miiskin.Gui;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miiskin.miiskin.R;
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
        if (moleSequenceListCursor == null) {

        } else {

        }
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fte_home,  container, false);
    }

    @Override
    public void onDataChanged(String dataId) {

    }
}
