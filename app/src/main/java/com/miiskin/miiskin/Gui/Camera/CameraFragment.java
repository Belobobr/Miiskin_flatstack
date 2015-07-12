package com.miiskin.miiskin.Gui.Camera;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.miiskin.miiskin.Data.AnalyticsNames;
import com.miiskin.miiskin.Data.L;
import com.miiskin.miiskin.Data.SavedPhotoInfo;
import com.miiskin.miiskin.Data.MoleData;
import com.miiskin.miiskin.Data.UserManager;
import com.miiskin.miiskin.Data.Utils;
import com.miiskin.miiskin.Gui.AcceptPhoto.AcceptPhotoActivity;
import com.miiskin.miiskin.Gui.General.PointedImageView;
import com.miiskin.miiskin.Helpers.BitmapDecoder;
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
    private DocCameraBorder mDocBorder;
    private ProgressBar mProgressBar;
    private FocusBorderView mFocusBorderView;
    private int mMode;
    private File mDirToSave;
    private View mAcceptPhoto;
    private View mCancelPhoto;
    private View mTakePhotoView;
    private PointedImageView bodyPartLocationPreview;

    private SavedPhotoInfo mSavedPhotoInfo;
    private MoleData mMoleData;
    private boolean mPhotoTaken = false;
    private String taskId;
    private boolean mIsSavePhoto = false;
    private Tracker mTracker;

    public static CameraFragment newInstance(int pMode, File dirToSavePhoto, MoleData moleData) {
        CameraFragment fragment = new CameraFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(CameraActivity.CAMERA_MODE, pMode);
        arguments.putSerializable(CameraActivity.DIR_TO_SAVE, dirToSavePhoto);
        arguments.putSerializable(CameraActivity.EXTRA_MOLE_DATE, moleData);
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
            mMoleData = (MoleData)arguments.getSerializable(CameraActivity.EXTRA_MOLE_DATE);
        }

        MiiskinApplication application = (MiiskinApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        mCameraStub = (FrameLayout) view.findViewById(R.id.camera_stub);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mDocBorder = (DocCameraBorder) view.findViewById(R.id.border);
        mFocusBorderView = (FocusBorderView) view.findViewById(R.id.focus_border);
        mTakePhotoView =  view.findViewById(R.id.take_photo);
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
                goToPreview();
            }
        });
        mCancelPhoto = view.findViewById(R.id.cancel_photo);
        mCancelPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDirToSave.exists()) {
                    mDirToSave.delete();
                }
                getActivity().finish();
            }
        });
        bodyPartLocationPreview = (PointedImageView)view.findViewById(R.id.bodyPartLocationPreview);
        bodyPartLocationPreview.setPointMode(PointedImageView.PointMode.NORMAL);
        bodyPartLocationPreview.post(new Runnable() {
            @Override
            public void run() {
                int resourceId = Utils.getImageResourceId(getActivity(), mMoleData.mBodyPart, UserManager.getInstance().getUserGender(), mMoleData.mBodyHalf, false);
                loadBodyImageView(bodyPartLocationPreview, resourceId);
                bodyPartLocationPreview.setPoint(mMoleData.bodyPartRelativePointX, mMoleData.bodyPartRelativePointY);
                bodyPartLocationPreview.invalidate();
            }
        });

        return view;
    }

    private void loadBodyImageView(PointedImageView bodyPartImageView, int resId) {
        final Bitmap bm = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), resId, bodyPartImageView.getWidth(), bodyPartImageView.getHeight());
        if (bm!=null) {
            bodyPartImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bodyPartImageView.setImageBitmap(bm);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        L.i("Setting screen name: " + AnalyticsNames.CAMERA_SCREEN);
        mTracker.setScreenName(AnalyticsNames.CAMERA_SCREEN);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

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
                    mCameraView.getAngle(), mDocBorder.getBorderRect(), mDirToSave, true, Long.parseLong(mMoleData.mId)}), taskId);
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
        mSavedPhotoInfo = (SavedPhotoInfo) TaskManager.getInstance(MiiskinApplication.getAppContext()).getDataById(taskId);
        if (mSavedPhotoInfo != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            //photoSavedSuccessFull(mId);
            mPhotoTaken = true;
            mIsSavePhoto = false;
            mCameraView.startPreview();

            if (mMode == CameraActivity.SINGLE_PHOTO) {
                //Можно завершить эту активность и перейти в предварительный просмотр
                goToPreview();
            }
        }
        if (mPhotoTaken) {
            mAcceptPhoto.setEnabled(true);
        } else {
            mAcceptPhoto.setEnabled(false);
        }

    }

    private void goToPreview() {
        Intent intent = new Intent(getActivity(), AcceptPhotoActivity.class);
        intent.putExtra(AcceptPhotoActivity.ARG_TAKEN_PHOTO_IMAGE_INFO, mSavedPhotoInfo);
        intent.putExtra(AcceptPhotoActivity.ARG_SEQUENCE_DATA, mMoleData);
        startActivity(intent);
    }


    private void photoSavedSuccessFull(String mPhotoId) {

    }




}
