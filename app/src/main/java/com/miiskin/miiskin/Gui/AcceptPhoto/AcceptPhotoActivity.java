package com.miiskin.miiskin.Gui.AcceptPhoto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.miiskin.miiskin.Data.SavedPhotoInfo;
import com.miiskin.miiskin.Data.MoleData;
import com.miiskin.miiskin.Gui.Camera.CameraFragment;
import com.miiskin.miiskin.Gui.MiiskinActivity;
import com.miiskin.miiskin.R;

public class AcceptPhotoActivity extends MiiskinActivity {

    public static final String ARG_TAKEN_PHOTO_IMAGE_INFO = "ARG_TAKEN_PHOTO_IMAGE_INFO";
    public static final String ARG_SEQUENCE_DATA = "ARG_SEQUENCE_DATA";

    SavedPhotoInfo mSavedPhotoInfo;
    MoleData mMoleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_photo);

        mSavedPhotoInfo = (SavedPhotoInfo)getIntent().getSerializableExtra(ARG_TAKEN_PHOTO_IMAGE_INFO);
        mMoleData = (MoleData)getIntent().getSerializableExtra(ARG_SEQUENCE_DATA);

        AcceptPhotoFragment fragment = (AcceptPhotoFragment) getFragmentManager().findFragmentByTag(CameraFragment.TAG);
        if (fragment == null) {
            fragment = AcceptPhotoFragment.newInstance(mSavedPhotoInfo, mMoleData);
            getFragmentManager().beginTransaction().replace(R.id.main_layout, fragment, CameraFragment.TAG).commit();
        }
    }
}
