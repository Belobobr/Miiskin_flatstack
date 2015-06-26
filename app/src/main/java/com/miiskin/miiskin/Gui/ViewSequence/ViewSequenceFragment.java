package com.miiskin.miiskin.Gui.ViewSequence;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miiskin.miiskin.Data.Paths;
import com.miiskin.miiskin.Data.SequenceData;
import com.miiskin.miiskin.Gui.Camera.CameraActivity;
import com.miiskin.miiskin.R;

import java.io.File;

/**
 * Created by Newshka on 26.06.2015.
 */
public class ViewSequenceFragment extends Fragment {
    public static final String EXTRA_SEQUENCE_DATA = "EXTRA_SEQUENCE_DATA";


    SequenceData mSequenceData;
    FloatingActionButton mFloatingActionButton;
    FloatingActionButton mSendToDoctor;
    FloatingActionButton mTakePhoto;
    CoordinatorLayout mCoordinatorLayout;
    private float mYDoctorInitialPosition;
    private float mYPhotoInitialPosition;
    private boolean mButtonsVisible = false;

    public static ViewSequenceFragment newInstance(SequenceData sequenceData) {
        ViewSequenceFragment fragment = new ViewSequenceFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(EXTRA_SEQUENCE_DATA, sequenceData);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mSequenceData = (SequenceData)getArguments().getSerializable(EXTRA_SEQUENCE_DATA);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        final ViewSequenceActivity viewSequenceActivity = (ViewSequenceActivity)getActivity();
        viewSequenceActivity.mActionBarToolbar.setTitle(mSequenceData.mBodyPart.toString());
        mFloatingActionButton.post(new Runnable() {
            @Override
            public void run() {
                mYDoctorInitialPosition = mSendToDoctor.getY();
                mYPhotoInitialPosition = mTakePhoto.getY();
            }
        });
        if (mButtonsVisible) {
            mSendToDoctor.setVisibility(View.VISIBLE);
            mTakePhoto.setVisibility(View.VISIBLE);
            mFloatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_clear));
        } else {
            mSendToDoctor.setVisibility(View.INVISIBLE);
            mTakePhoto.setVisibility(View.INVISIBLE);
            mFloatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_add));
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ViewSequenceActivity viewSequenceActivity = (ViewSequenceActivity)getActivity();
        viewSequenceActivity.mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        viewSequenceActivity.mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mButtonsVisible) {
                    showButtons();
                } else {
                    hideButtons();
                }

            }
        });
        mSendToDoctor = (FloatingActionButton)view.findViewById(R.id.sendDoctor);
        mSendToDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToDoctor();
            }
        });
        mTakePhoto = (FloatingActionButton)view.findViewById(R.id.takePhoto);
        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        mCoordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.coordinatorLayout);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_sequence, container, false);
    }

    private void sendToDoctor() {

    }

    private void takePhoto() {
        //Determine folder where photo will be saved
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        if (path.exists()) {
            File sequencePhotoDir = new File(path, Paths.getRelativeDirForSequence(mSequenceData.mId));
            if (sequencePhotoDir.exists()) {
                savePhotoToFile(sequencePhotoDir);
            } else {
                boolean success = sequencePhotoDir.mkdirs();
                if (success) {
                    savePhotoToFile(sequencePhotoDir);
                } else {
                    Toast toast = Toast.makeText(getActivity(), R.string.cant_create_photo, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }

    private void savePhotoToDir(File file) {
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra(CameraActivity.DIR_TO_SAVE, file);
        startActivity(intent);
    }

    private void savePhotoToFile(File dir) {
        File tempPhotoPath = dir;
        //генерируем имя файла
        int indexPhoto = tempPhotoPath.listFiles().length + 1;
        String fileName = tempPhotoPath.getAbsolutePath() + "/" + indexPhoto + ".png";
        File fileSavedTo = new File(fileName);
        Intent intent = new Intent(getActivity(), CameraActivity.class);
        intent.putExtra(CameraActivity.DIR_TO_SAVE, fileSavedTo);
        intent.putExtra(CameraActivity.EXTRA_SEQUENCE_DATA, mSequenceData);
        startActivity(intent);

    }

    private void showButtons() {
        float centerFabY = mFloatingActionButton.getY() + mFloatingActionButton.getHeight() / 2;

        float yDoctorFrom = centerFabY - mSendToDoctor.getHeight() / 2;
        float yPhotoFrom = centerFabY - mTakePhoto.getHeight() / 2;

        ValueAnimator doctorTransitionAnimator = ObjectAnimator.ofFloat(mSendToDoctor, "y", yDoctorFrom, mYDoctorInitialPosition);
        ValueAnimator photoTransitionAnimator = ObjectAnimator.ofFloat(mTakePhoto, "y", yPhotoFrom, mYPhotoInitialPosition);

        AnimatorSet animPhotoAndDoctorAppearing = new AnimatorSet();
        animPhotoAndDoctorAppearing.playTogether(doctorTransitionAnimator, photoTransitionAnimator);
        animPhotoAndDoctorAppearing.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mSendToDoctor.setVisibility(View.VISIBLE);
                mTakePhoto.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mButtonsVisible = true;
                mFloatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_clear));
            }
        });
        animPhotoAndDoctorAppearing.setDuration(250);
        animPhotoAndDoctorAppearing.start();
    }

    private void hideButtons() {
        float centerFabY = mFloatingActionButton.getY() + mFloatingActionButton.getHeight() / 2;

        float yDoctorTo = centerFabY - mSendToDoctor.getHeight() / 2;
        float yPhotoTo = centerFabY - mTakePhoto.getHeight() / 2;

        ValueAnimator doctorTransitionAnimator = ObjectAnimator.ofFloat(mSendToDoctor, "y", mYDoctorInitialPosition, yDoctorTo);
        ValueAnimator photoTransitionAnimator = ObjectAnimator.ofFloat(mTakePhoto, "y", mYPhotoInitialPosition, yPhotoTo);

        AnimatorSet animPhotoAndDoctorAppearing = new AnimatorSet();
        animPhotoAndDoctorAppearing.playTogether(doctorTransitionAnimator, photoTransitionAnimator);
        animPhotoAndDoctorAppearing.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                mSendToDoctor.setVisibility(View.INVISIBLE);
                mTakePhoto.setVisibility(View.INVISIBLE);
                mButtonsVisible = false;
                mFloatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_add));
            }
        });
        animPhotoAndDoctorAppearing.setDuration(250);
        animPhotoAndDoctorAppearing.start();
    }
}
