package com.miiskin.miiskin.Gui.Camera;

import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.miiskin.miiskin.Data.SequenceData;
import com.miiskin.miiskin.Gui.Home.DisclaimerDialogFragment;
import com.miiskin.miiskin.Gui.Home.FTEHomeFragment;
import com.miiskin.miiskin.Gui.Home.HomeFragment;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.Preferences;

import java.io.File;

/**
 * Created by Newshka on 26.06.2015.
 */
public class CameraActivity extends AppCompatActivity implements FteCameraTipsFragment.FteCameraTipsListener{
    public static final String EXTRA_SEQUENCE_DATA = "EXTRA_SEQUENCE_DATA";
    public static final String DIR_TO_SAVE = "DIR_TO_SAVE";

    public static final String CAMERA_MODE = "CAMERA_MODE";
    public static final int MULTI_PHOTO = 0;
    public static final int SINGLE_PHOTO = 1;

    private int mMode;
    private File dirToSavePhoto;
    private SequenceData mSequenceData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_stub_tablet);
        //создаем фрагмент, к тором реализована вся разметка

        mMode = getIntent().getIntExtra(CAMERA_MODE, MULTI_PHOTO);
        dirToSavePhoto = (File)getIntent().getSerializableExtra(DIR_TO_SAVE);
        mSequenceData = (SequenceData)getIntent().getSerializableExtra(EXTRA_SEQUENCE_DATA);

        CameraFragment fragment = (CameraFragment) getFragmentManager().findFragmentByTag(CameraFragment.TAG);
        if (fragment == null) {
            fragment = CameraFragment.newInstance(mMode, dirToSavePhoto, mSequenceData);
            getFragmentManager().beginTransaction().replace(R.id.fragment_stub, fragment, CameraFragment.TAG).commit();
        }

        SharedPreferences settings = getSharedPreferences(Preferences.MAIN_PREFERENCES, 0);
        boolean fteCamera = settings.getBoolean(Preferences.FirstTimeUse.FTE_SHOW_CAMERA_TIPS, true);


        if (savedInstanceState == null) {
            if (fteCamera) {
                DialogFragment dialog = new FteCameraTipsFragment();
                dialog.show(getFragmentManager(), FteCameraTipsFragment.TAG);
            }
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        setFteCameraTips(false);
    }

    @Override
    public void onDialogBackButtonPressed() {
        setFteCameraTips(false);
    }

    private void setFteCameraTips(boolean fteCameraTips) {
        SharedPreferences settings = getSharedPreferences(Preferences.MAIN_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(Preferences.FirstTimeUse.FTE_SHOW_CAMERA_TIPS, fteCameraTips);
        editor.commit();
    }
}
