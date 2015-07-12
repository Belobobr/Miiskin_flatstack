package com.miiskin.miiskin.Gui.CreateSequence;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.miiskin.miiskin.Data.AnalyticsNames;
import com.miiskin.miiskin.Data.BodyHalf;
import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Data.L;
import com.miiskin.miiskin.Data.MoleData;
import com.miiskin.miiskin.Gui.MiiskinActivity;
import com.miiskin.miiskin.Gui.ViewSequence.ViewMoleActivity;
import com.miiskin.miiskin.MiiskinApplication;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.Task.SaveMoleToDatabaseTask;
import com.miiskin.miiskin.Storage.Task.TaskManager;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Newshka on 24.06.2015.
 */
public class CreateMoleActivity extends MiiskinActivity implements GeneralAreaFragment.GeneralAreaSelectedListener, SpecificLocationFragment.SpecificLocationSelectedListener,
    TaskManager.DataChangeListener {

    Toolbar mActionBarToolbar;

    private static final String SEQUENCE_DATA_TAG = "SEQUENCE_DATA_TAG ";
    private static final String TASK_ID = "TASK_ID";
    private MoleData mMoleData;
    private String taskId;
    private Tracker mTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)  {
            mMoleData = (MoleData)savedInstanceState.getSerializable(SEQUENCE_DATA_TAG);
            taskId = savedInstanceState.getString(TASK_ID);
        } else {
            mMoleData = new MoleData();
        }

        setContentView(R.layout.activity_create_secuence);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_layout);
        if (fragment == null) {
            fragment = GeneralAreaFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.main_layout, fragment).commit();
        }

        MiiskinApplication application = (MiiskinApplication)getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SEQUENCE_DATA_TAG, mMoleData);
        outState.putSerializable(TASK_ID, taskId);
    }

    @Override
    public void onDataChanged(String dataId) {
        if (dataId.equals(taskId)) {
            updateUi();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        TaskManager.getInstance(getApplicationContext()).removeDataChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TaskManager.getInstance(getApplicationContext()).addDataChangeListener(this);
        updateUi();
    }

    private void updateUi() {
        Long moleId = (Long)TaskManager.getInstance(getApplicationContext()).getDataById(taskId);
        mMoleData.mId = String.valueOf(moleId);
        if (moleId != null) {
            if (moleId != -1) {
                showCreatedMoleScreen(moleId);
                Long moleCount = moleId + 1;
                L.i("Custom dimension change: " + AnalyticsNames.CustomDimension.SEQUENCES_CREATED + ": " + moleCount);
                L.i("Custom metric change: " + AnalyticsNames.CustomMetrics.SEQUENCES_CREATED + ": " + moleCount);
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory(AnalyticsNames.EventCategory.SEQUENCES)
                        .setAction(AnalyticsNames.EventAction.SEQUENCE_CREATED)
                        .setCustomDimension(AnalyticsNames.CustomDimension.SEQUENCES_CREATED_ID, moleCount.toString())
                        .setCustomMetric(AnalyticsNames.CustomMetrics.SEQUENCES_CREATED_ID, moleCount)
                        .setValue(1)
                        .build());
            }
            else {
                finish();
                Toast toast = Toast.makeText(this, getString(R.string.error_creating_mole), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void goToSelectSpecificLocationFragment() {
        Fragment fragment = SpecificLocationFragment.newInstance(mMoleData.mBodyPart, mMoleData.mBodyHalf);
        getFragmentManager().beginTransaction().replace(R.id.main_layout, fragment, SpecificLocationFragment.TAG).addToBackStack(null).commit();
    }

    public void showCreatedMoleScreen(long moleId) {
        Intent intent = new Intent(this, ViewMoleActivity.class);
        intent.putExtra(ViewMoleActivity.EXTRA_MOLE_ID, moleId);
        startActivity(intent);
    }

    public void saveCreatedMoleToDatabase() {
        taskId = UUID.randomUUID().toString();
        TaskManager.getInstance(getApplicationContext()).executeTask(new SaveMoleToDatabaseTask(getApplicationContext(), new Object[] {mMoleData}), taskId);
    }

    @Override
    public void onGeneralAreaSelected(BodyPart bodyPart, BodyHalf bodyHalf) {
        mMoleData.mBodyPart = bodyPart;
        mMoleData.mBodyHalf = bodyHalf;
        goToSelectSpecificLocationFragment();
    }

    @Override
    public void onSpecificLocationSelected(float bodyPartRelativePointX, float bodyPartRelativePointY) {
        mMoleData.bodyPartRelativePointX = bodyPartRelativePointX;
        mMoleData.bodyPartRelativePointY = bodyPartRelativePointY;
        mMoleData.mDateOfCreation = new Date();
        saveCreatedMoleToDatabase();
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag(SpecificLocationFragment.TAG) != null) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
