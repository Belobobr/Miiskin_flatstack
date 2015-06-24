package com.miiskin.miiskin.Gui.Home;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.Task.LoadSequenceList;
import com.miiskin.miiskin.Storage.Task.TaskManager;

/**
 * Created by Newshka on 23.06.2015.
 */
public class HomeFragment extends Fragment implements TaskManager.DataChangeListener {

    Cursor moleSequenceListCursor;
    ListView mListView;
    CoordinatorLayout mEmptyView;
    FrameLayout mProgressView;
    FloatingActionButton mFloatingActionButton;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (moleSequenceListCursor == null) {
            TaskManager.getInstance(getActivity().getApplicationContext()).executeTask(LoadSequenceList.TASK_ID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        TaskManager.getInstance(getActivity().getApplicationContext()).addDataChangeListener(this);
        updateGui();
    }

    @Override
    public void onPause() {
        super.onPause();
        TaskManager.getInstance(getActivity().getApplicationContext()).removeDataChangeListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,  container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView)view.findViewById(R.id.listView);
        mEmptyView = (CoordinatorLayout)view.findViewById(R.id.emptyView);
        mProgressView = (FrameLayout)view.findViewById(R.id.progressView);
        mFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        mFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab));
    }

    @Override
    public void onDataChanged(String dataId) {
        if (dataId.equals(LoadSequenceList.TASK_ID)) {
            updateGui();
        }
    }

    private void updateGui() {
        moleSequenceListCursor  = TaskManager.getInstance(getActivity().getApplicationContext()).getDataById(LoadSequenceList.TASK_ID);
        if (moleSequenceListCursor == null) {
            mProgressView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
        } else {
            if (moleSequenceListCursor.getCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
            } else {
                mListView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
            }
        }
    }
}
