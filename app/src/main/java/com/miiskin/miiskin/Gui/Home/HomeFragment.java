package com.miiskin.miiskin.Gui.Home;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.miiskin.miiskin.Data.AnalyticsNames;
import com.miiskin.miiskin.Data.L;
import com.miiskin.miiskin.Gui.CreateSequence.CreateMoleActivity;
import com.miiskin.miiskin.Gui.ViewSequence.ViewMoleActivity;
import com.miiskin.miiskin.MiiskinApplication;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract;
import com.miiskin.miiskin.Storage.Task.LoadMolesListTask;
import com.miiskin.miiskin.Storage.Task.TaskManager;

/**
 * Created by Newshka on 23.06.2015.
 */
public class HomeFragment extends Fragment implements TaskManager.DataChangeListener {

    Cursor moleSequenceListCursor;
    ListView mManySequenceListView;
    CoordinatorLayout mManySequence;
    CoordinatorLayout mEmptyView;
    FrameLayout mProgressView;
    FloatingActionButton mNoSequenceFab;
    FloatingActionButton mManySequenceFab;
    MoleCursorAdapter mMoleCursorAdapter;
    private Tracker mTracker;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (moleSequenceListCursor == null) {
            TaskManager.getInstance(getActivity().getApplicationContext()).executeTask(new LoadMolesListTask(getActivity().getApplicationContext()), LoadMolesListTask.TASK_ID);
        }

        MiiskinApplication application = (MiiskinApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();
        L.i("Setting screen name: " + AnalyticsNames.HOME_ACTIVITY_SCREEN_NAME);
        mTracker.setScreenName(AnalyticsNames.HOME_ACTIVITY_SCREEN_NAME);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

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
        mManySequenceListView = (ListView)view.findViewById(R.id.manySequenceListView);
        mManySequence = (CoordinatorLayout)view.findViewById(R.id.manySequence);
        mEmptyView = (CoordinatorLayout)view.findViewById(R.id.emptyView);
        mProgressView = (FrameLayout)view.findViewById(R.id.progressView);
        mNoSequenceFab = (FloatingActionButton)view.findViewById(R.id.fabNoSequence);
        mNoSequenceFab.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab));
        mNoSequenceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateMoleActivity.class);
                startActivity(intent);
            }
        });
        mManySequenceFab = (FloatingActionButton)view.findViewById(R.id.fabManySequence);
        mManySequenceFab.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab));
        mManySequenceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateMoleActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDataChanged(String dataId) {
        if (dataId.equals(LoadMolesListTask.TASK_ID)) {
            updateGui();
        }
    }

    private void updateGui() {
        moleSequenceListCursor  = (Cursor)TaskManager.getInstance(getActivity().getApplicationContext()).getDataById(LoadMolesListTask.TASK_ID);
        if (moleSequenceListCursor == null) {
            mProgressView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            mManySequence.setVisibility(View.GONE);
        } else {
            if (moleSequenceListCursor.getCount() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
                mManySequence.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
            } else {
                if (mMoleCursorAdapter == null) {
                    mMoleCursorAdapter = new MoleCursorAdapter(getActivity(), moleSequenceListCursor);
                } else {
                    mMoleCursorAdapter.changeCursor(moleSequenceListCursor);
                }
                mManySequenceListView.setAdapter(mMoleCursorAdapter);
                mManySequenceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        moleSequenceListCursor.moveToPosition(position);
                        Intent intent = new Intent(HomeFragment.this.getActivity(), ViewMoleActivity.class);
                        intent.putExtra(ViewMoleActivity.EXTRA_MOLE_ID, Long.parseLong(moleSequenceListCursor.getString(moleSequenceListCursor.getColumnIndex(MiiskinDatabaseContract.Mole._ID))));
                        startActivity(intent);
                    }
                });
                mManySequence.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
            }
        }
    }
}
