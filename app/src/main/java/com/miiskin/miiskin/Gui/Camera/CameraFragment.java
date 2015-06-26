package com.miiskin.miiskin.Gui.Camera;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.miiskin.miiskin.Data.SavedPhotoInfo;
import com.miiskin.miiskin.Gui.AcceptPhoto.AcceptPhotoActivity;
import com.miiskin.miiskin.MiiskinApplication;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.Task.SavePhotoFileTask;
import com.miiskin.miiskin.Storage.Task.TaskManager;

import java.io.File;
import java.util.UUID;


/**
 * Фрагмент для работы с камерой.
 * Created by Kalashnikov S.A. on 24.10.2014.
 */
public class CameraFragment extends Fragment implements TaskManager.DataChangeListener{

    public static final String TAG = "CameraFragment";
    public FrameLayout mCameraStub;
    private CameraView mCameraView;
    private ImageView mTakePhotoView;
    private DocCameraBorder mDocBorder;
    private ProgressBar mProgressBar;
    private boolean mIsSavePhoto = false;
    private FocusBorderView mFocusBorderView;
    private boolean mPhotoTaken = false;
    private String taskId;

    private int mMode;
    private File mDirToSave;
    private View mAcceptPhoto;
    private View mCancelPhoto;

    public static CameraFragment newInstance(int pMode, File dirToSavePhoto) {
        CameraFragment fragment = new CameraFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(CameraActivity.CAMERA_MODE, pMode);
        arguments.putSerializable(CameraActivity.DIR_TO_SAVE, dirToSavePhoto);
        fragment.setArguments(arguments);
        return fragment;
    }

    public CameraFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMode = arguments.getInt(CameraActivity.CAMERA_MODE, CameraActivity.MULTI_PHOTO);
            mDirToSave = (File)arguments.getSerializable(CameraActivity.DIR_TO_SAVE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        mCameraStub = (FrameLayout) view.findViewById(R.id.camera_stub);
        mTakePhotoView = (ImageView) view.findViewById(R.id.take_photo);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mDocBorder = (DocCameraBorder) view.findViewById(R.id.border);
        mFocusBorderView = (FocusBorderView) view.findViewById(R.id.focus_border);

        mTakePhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraView != null && !mIsSavePhoto) {
                    mIsSavePhoto = true;
                    mProgressBar.setVisibility(View.VISIBLE);
                    mCameraView.setJpgCallback(mJpgCallback);
                    mCameraView.takePicture();
                }
            }
        });
        mAcceptPhoto = view.findViewById(R.id.accept_photo);
        mAcceptPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AcceptPhotoActivity.class);
                startActivity(intent);
            }
        });
        mCancelPhoto = view.findViewById(R.id.cancel_photo);
        mCancelPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        TaskManager.getInstance(MiiskinApplication.getAppContext()).addDataChangeListener(this);
        mCameraView = new CameraView(this.getActivity(), 0, CameraView.LayoutMode.FitToParent);
        mCameraView.setFocusBorderView(mFocusBorderView);
        FrameLayout.LayoutParams previewLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        previewLayoutParams.gravity = Gravity.CENTER;
        mCameraStub.addView(mCameraView, 0, previewLayoutParams);
        updateUi();
    }

    @Override
    public void onPause() {
        super.onPause();
        TaskManager.getInstance(MiiskinApplication.getAppContext()).removeDataChangeListener(this);
        mCameraView.stop();
        mCameraStub.removeView(mCameraView);
        mCameraView = null;
    }

    Camera.PictureCallback mJpgCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point displaySize = new Point();
            display.getSize(displaySize);
            taskId = UUID.randomUUID().toString();
            TaskManager.getInstance(MiiskinApplication.getAppContext()).executeTask(new SavePhotoFileTask(getActivity(), new Object[]{data, displaySize, mCameraView.getRealViewSize(),
                    mCameraView.getAngle(), mDocBorder.getBorderRect(), mDirToSave, true}), taskId);
        }
    };

    @Override
    public void onDataChanged(String dataId) {
        if (dataId.equals(taskId)) {
            updateUi();
        }
    }

    private void updateUi() {
        //То как мне получить результат задачи
        SavedPhotoInfo savedPhotoInfo = (SavedPhotoInfo)TaskManager.getInstance(MiiskinApplication.getAppContext()).getDataById(taskId);
        if (savedPhotoInfo != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            //photoSavedSuccessFull(mId);
            mPhotoTaken = true;
            mIsSavePhoto = false;
            mCameraView.startPreview();

            if (mMode == CameraActivity.SINGLE_PHOTO) {
                //Можно завершить эту активность и перейти в предварительный просмотр
                Intent intent = new Intent();
                intent.putExtra(CameraActivity.ARG_TAKEN_PHOTO_IMAGE_INFO, savedPhotoInfo);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        }
        if (mPhotoTaken) {
            mAcceptPhoto.setEnabled(true);
        } else {
            mAcceptPhoto.setEnabled(false);
        }

    }

    private void photoSavedSuccessFull(String mPhotoId) {

    }




}
