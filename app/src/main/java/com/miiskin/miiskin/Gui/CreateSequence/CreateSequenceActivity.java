package com.miiskin.miiskin.Gui.CreateSequence;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Gui.ViewSequence.ViewSequenceActivity;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.Task.SaveCreatedSequenceToDatabase;
import com.miiskin.miiskin.Storage.Task.TaskManager;

import java.io.Serializable;

/**
 * Created by Newshka on 24.06.2015.
 */
public class CreateSequenceActivity extends AppCompatActivity implements GeneralAreaFragment.GeneralAreaSelectedListener, SpecificLocationFragment.SpecificLocationSelectedListener,
    TaskManager.DataChangeListener {

    Toolbar mActionBarToolbar;

    public static class SequenceData implements Serializable {
        BodyPart mBodyPart;
    }

    private static final String SEQUENCE_DATA_TAG = "SEQUENCE_DATA_TAG ";
    private SequenceData mSequenceData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)  {
            mSequenceData = (SequenceData)savedInstanceState.getSerializable(SEQUENCE_DATA_TAG);
        } else {
            mSequenceData = new SequenceData();
        }

        setContentView(R.layout.activity_create_secuence);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mActionBarToolbar);

        Fragment fragment = getFragmentManager().findFragmentById(R.id.main_layout);
        if (fragment == null) {
            fragment = GeneralAreaFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.main_layout, fragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SEQUENCE_DATA_TAG, mSequenceData);
    }

    @Override
    public void onDataChanged(String dataId) {
        if (dataId.equals(SaveCreatedSequenceToDatabase.TASK_ID)) {
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
        Integer sequenceId = (Integer)TaskManager.getInstance(getApplicationContext()).getDataById(SaveCreatedSequenceToDatabase.TASK_ID);
        if (sequenceId != null) {
            showCreatedSequenceScreen(sequenceId);
        }

    }

    public void goToSelectSpecificLocationFragment() {
        Fragment fragment = SpecificLocationFragment.newInstance(mSequenceData.mBodyPart);
        getFragmentManager().beginTransaction().replace(R.id.main_layout, fragment, SpecificLocationFragment.TAG).addToBackStack(null).commit();
    }

    public void showCreatedSequenceScreen(Integer sequenceId) {
        Intent intent = new Intent(this, ViewSequenceActivity.class);
        startActivity(intent);
    }

    public void saveCreatedSequenceToDatabase() {
        TaskManager.getInstance(getApplicationContext()).executeTask(SaveCreatedSequenceToDatabase.TASK_ID);
    }

    @Override
    public void onGeneralAreaSelected(BodyPart bodyPart) {
        mSequenceData.mBodyPart = bodyPart;
        goToSelectSpecificLocationFragment();
    }

    @Override
    public void onSpecificLocationSelected() {
        saveCreatedSequenceToDatabase();
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
