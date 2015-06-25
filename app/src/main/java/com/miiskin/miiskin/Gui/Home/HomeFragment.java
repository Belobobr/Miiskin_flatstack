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

import com.miiskin.miiskin.Data.SequenceData;
import com.miiskin.miiskin.Gui.CreateSequence.CreateSequenceActivity;
import com.miiskin.miiskin.Gui.ViewSequence.ViewSequenceActivity;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.Task.LoadSequenceList;
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
    SequenceCursorAdapter mSequenceCursorAdapter;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (moleSequenceListCursor == null) {
            TaskManager.getInstance(getActivity().getApplicationContext()).executeTask(new LoadSequenceList(getActivity().getApplicationContext()), LoadSequenceList.TASK_ID);
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
        mManySequenceListView = (ListView)view.findViewById(R.id.manySequenceListView);
        mManySequence = (CoordinatorLayout)view.findViewById(R.id.manySequence);
        mEmptyView = (CoordinatorLayout)view.findViewById(R.id.emptyView);
        mProgressView = (FrameLayout)view.findViewById(R.id.progressView);
        mNoSequenceFab = (FloatingActionButton)view.findViewById(R.id.fabNoSequence);
        mNoSequenceFab.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab));
        mNoSequenceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateSequenceActivity.class);
                startActivity(intent);
            }
        });
        mManySequenceFab = (FloatingActionButton)view.findViewById(R.id.fabManySequence);
        mManySequenceFab.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab));
        mManySequenceFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateSequenceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDataChanged(String dataId) {
        if (dataId.equals(LoadSequenceList.TASK_ID)) {
            updateGui();
        }
    }

    private void updateGui() {
        moleSequenceListCursor  = (Cursor)TaskManager.getInstance(getActivity().getApplicationContext()).getDataById(LoadSequenceList.TASK_ID);
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
                if (mSequenceCursorAdapter == null) {
                    mSequenceCursorAdapter = new SequenceCursorAdapter(getActivity(), moleSequenceListCursor);
                } else {
                    mSequenceCursorAdapter.changeCursor(moleSequenceListCursor);
                }
                mManySequenceListView.setAdapter(mSequenceCursorAdapter);
                mManySequenceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        moleSequenceListCursor.moveToPosition(position);
                        SequenceData sequenceData = SequenceData.createFromCursor(moleSequenceListCursor);
                        Intent intent = new Intent(HomeFragment.this.getActivity(), ViewSequenceActivity.class);
                        intent.putExtra(ViewSequenceActivity.EXTRA_SEQUENCE_DATA, sequenceData);
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
