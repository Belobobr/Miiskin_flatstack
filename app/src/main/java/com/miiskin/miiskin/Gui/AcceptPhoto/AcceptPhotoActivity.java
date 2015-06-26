package com.miiskin.miiskin.Gui.AcceptPhoto;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.miiskin.miiskin.Data.SavedPhotoInfo;
import com.miiskin.miiskin.Data.SequenceData;
import com.miiskin.miiskin.Gui.Camera.CameraFragment;
import com.miiskin.miiskin.R;

import java.io.File;

public class AcceptPhotoActivity extends AppCompatActivity {

    public static final String ARG_TAKEN_PHOTO_IMAGE_INFO = "ARG_TAKEN_PHOTO_IMAGE_INFO";
    public static final String ARG_SEQUENCE_DATA = "ARG_SEQUENCE_DATA";

    SavedPhotoInfo mSavedPhotoInfo;
    SequenceData mSequenceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_photo);

        mSavedPhotoInfo = (SavedPhotoInfo)getIntent().getSerializableExtra(ARG_TAKEN_PHOTO_IMAGE_INFO);
        mSequenceData = (SequenceData)getIntent().getSerializableExtra(ARG_SEQUENCE_DATA);

        AcceptPhotoFragment fragment = (AcceptPhotoFragment) getFragmentManager().findFragmentByTag(CameraFragment.TAG);
        if (fragment == null) {
            fragment = AcceptPhotoFragment.newInstance(mSavedPhotoInfo, mSequenceData);
            getFragmentManager().beginTransaction().replace(R.id.main_layout, fragment, CameraFragment.TAG).commit();
        }
    }
}
