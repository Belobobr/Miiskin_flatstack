package com.miiskin.miiskin.Gui.AcceptPhoto;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.miiskin.miiskin.Data.SavedPhotoInfo;
import com.miiskin.miiskin.Data.MoleData;
import com.miiskin.miiskin.Gui.ViewSequence.ViewMoleActivity;
import com.miiskin.miiskin.Helpers.BitmapDecoder;
import com.miiskin.miiskin.R;

import java.io.File;

/**
 * Created by Newshka on 26.06.2015.
 */
public class AcceptPhotoFragment extends Fragment {

    SavedPhotoInfo mSavedPhotoInfo;
    MoleData mMoleData;
    View mRetakePhotoButton;
    View mAcceptPhotoButton;
    View mCancelPhotoButton;
    ImageView mImageView;

    public static AcceptPhotoFragment newInstance(SavedPhotoInfo savedPhotoInfo, MoleData moleData) {
        AcceptPhotoFragment fragment = new AcceptPhotoFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(AcceptPhotoActivity.ARG_TAKEN_PHOTO_IMAGE_INFO, savedPhotoInfo);
        arguments.putSerializable(AcceptPhotoActivity.ARG_SEQUENCE_DATA, moleData);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mSavedPhotoInfo = (SavedPhotoInfo)arguments.getSerializable(AcceptPhotoActivity.ARG_TAKEN_PHOTO_IMAGE_INFO);
            mMoleData = (MoleData)arguments.getSerializable(AcceptPhotoActivity.ARG_SEQUENCE_DATA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageView.post(new Runnable() {
            @Override
            public void run() {

                final Bitmap bitmap = BitmapDecoder.decodeBitmapFromFile(mSavedPhotoInfo.mPath, mImageView.getWidth(), mImageView.getHeight());
                if (bitmap!=null) {
                    mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    mImageView.setImageBitmap(bitmap);
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accept_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRetakePhotoButton = view.findViewById(R.id.retake_photo);
        mRetakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mAcceptPhotoButton = view.findViewById(R.id.accept_photo);
        mAcceptPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getActivity(), ViewMoleActivity.class);
                intent.putExtra(ViewMoleActivity.EXTRA_MOLE_ID, Long.parseLong(mMoleData.mId));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        mCancelPhotoButton = view.findViewById(R.id.cancel_photo);
        mCancelPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePhoto();
                Intent intent  = new Intent(getActivity(), ViewMoleActivity.class);
                intent.putExtra(ViewMoleActivity.EXTRA_MOLE_ID, Long.parseLong(mMoleData.mId));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mImageView = (ImageView)view.findViewById(R.id.previewImage);
    }

    private void removePhoto() {
        File file = new File(mSavedPhotoInfo.mPath);
        if (file.exists()) {
            file.delete();
        }
    }
}
