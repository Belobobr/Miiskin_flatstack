package com.miiskin.miiskin.Gui.Camera;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.miiskin.miiskin.Data.L;
import com.miiskin.miiskin.MiiskinApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Вьюха для фотокамеры.
 * Created by Kalashnikov S.A. on 17.11.2014.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreview";
    private static final String LOG_TAG = "CameraPreviewSample";
    private static final String CAMERA_PARAM_ORIENTATION = "orientation";
    private static final String CAMERA_PARAM_LANDSCAPE = "landscape";
    private static final String CAMERA_PARAM_PORTRAIT = "portrait";
    protected Activity mActivity;
    private SurfaceHolder mHolder;
    protected Camera mCamera;
    protected List<Camera.Size> mPreviewSizeList;
    protected List<Camera.Size> mPictureSizeList;
    protected Camera.Size mPreviewSize;
    protected Camera.Size mPictureSize;
    protected Point mViewSize;
    private int mSurfaceChangedCallDepth = 0;
    private int mCameraId;
    private LayoutMode mLayoutMode;
    private int mCenterPosX = -1;
    private int mCenterPosY;
    private FocusBorderView mFocusBorderView;
    private int mAngle;

    public void setFocusBorderView(FocusBorderView pFocusBorderView) {
        mFocusBorderView = pFocusBorderView;
    }

    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {

        @Override
        public void onShutter() {

        }
    };

    private Camera.PictureCallback mJpgCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    private Camera.PictureCallback mRawCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

        }
    };

    PreviewReadyCallback mPreviewReadyCallback = null;

    public static enum LayoutMode {
        FitToParent, // Scale to the size that no side is larger than the parent
        NoBlank // Scale to the size that no side is smaller than the parent
    }


    public interface PreviewReadyCallback {
        public void onPreviewReady();
    }

    /**
     * State flag: true when surface's layout size is set and surfaceChanged()
     * process has not been completed.
     */
    protected boolean mSurfaceConfiguring = false;

    public CameraView(Activity activity, int cameraId, LayoutMode mode) {
        super(activity); // Always necessary
        mActivity = activity;
        mLayoutMode = mode;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (Camera.getNumberOfCameras() > cameraId) {
                mCameraId = cameraId;
            } else {
                mCameraId = 0;
            }
        } else {
            mCameraId = 0;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            mCamera = Camera.open(mCameraId);
        } else {
            mCamera = Camera.open();
        }
        Camera.Parameters cameraParams = mCamera.getParameters();
        mPreviewSizeList = cameraParams.getSupportedPreviewSizes();
        Collections.sort(mPreviewSizeList, new CameraSizeComparator());

        mPictureSizeList = cameraParams.getSupportedPictureSizes();
      /*  L.w("LIST PREVIEW SIZE:");
        for (Camera.Size size : mPreviewSizeList) {
            float r = (float) size.width / size.height;
            L.i(" - " + size.width + "*" + size.height + " (ratio = " + r + ")");
        }


        L.w("LIST CAMERA SIZE:");
        for (Camera.Size size : mPictureSizeList) {
            float r = (float) size.width / size.height;
            L.w(" - " + size.width + "*" + size.height + " (ratio = " + r + ")");
        }*/
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceChangedCallDepth++;
        doSurfaceChanged(width, height);
        mSurfaceChangedCallDepth--;
    }

    private void doSurfaceChanged(int width, int height) {
        L.i("doSurfaceChanged = " + width + "*" + height);
        mCamera.stopPreview();
        Camera.Parameters cameraParams = mCamera.getParameters();
        boolean portrait = isPortrait();
        if (!mSurfaceConfiguring) {
            Camera.Size pictureSize = determinePictureSize(mPictureSizeList);
            Camera.Size previewSize = determinePreviewSize(pictureSize);
            mPreviewSize = previewSize;
            mPictureSize = pictureSize;
            mSurfaceConfiguring = adjustSurfaceLayoutSize(previewSize, portrait, width, height, mPictureSize);
            if (mSurfaceConfiguring && (mSurfaceChangedCallDepth <= 1)) {
                return;
            }
        }

        configureCameraParameters(cameraParams, portrait);
        mSurfaceConfiguring = false;

        try {
            mCamera.startPreview();
        } catch (Exception e) {
            // Remove failed size
            mPreviewSizeList.remove(mPreviewSize);
            mPreviewSize = null;

            // Reconfigure
            if (mPreviewSizeList.size() > 0) { // prevent infinite loop
                surfaceChanged(null, 0, width, height);
            } else {
                Toast.makeText(mActivity, "Can't start preview", Toast.LENGTH_LONG).show();
                Log.w(LOG_TAG, "Gave up starting preview");
            }
        }

        if (null != mPreviewReadyCallback) {
            mPreviewReadyCallback.onPreviewReady();
        }
    }

    /**
     * Определяем наиболее подходящие размер области просмотра
     */
    protected Camera.Size determinePreviewSize(Camera.Size pPictureSize) {
        L.w("****** determinePreviewSize");
        float pPictureRatio = (float) pPictureSize.width / pPictureSize.height;
        Camera.Size retSize = null;
        float retRatio = 0;
        for (Camera.Size size : mPreviewSizeList) {
            L.w("****** analyze " + size.width + "*" + size.height);
            if (retSize == null) {
                retSize = size;
                retRatio = (float) size.width / size.height;
            } else {
                float ratio = (float) size.width / size.height;
                if (Math.abs(pPictureRatio - ratio) <= Math.abs(pPictureRatio - retRatio)) {
                    retSize = size;
                    retRatio = (float) size.width / size.height;
                }
            }
        }
        L.i("Установлен размер просмотра " + retSize.width + "*" + retSize.height);
        return retSize;
    }

    /**
     * Выбирает наиболее подходящий размер получаемого изображения
     * берем макрисальный размер кадра который наиболее соответсвует соотношению сторон просмотра
     *
     * @return
     */
    protected Camera.Size determinePictureSize(List<Camera.Size> pPictureSizeList) {
        L.w(" **************   determinePictureSize");

        Camera.Size retSize = null;
        for (Camera.Size size : pPictureSizeList) {

            if (retSize == null) {
                retSize = size;
            } else {
                if ((retSize.height + retSize.width) < (size.height + size.width)) {
                    retSize = size;
                }
            }
        }
        L.i("Установлен размер изображения " + retSize.width + "*" + retSize.height);
        return retSize;
    }

    /**
     * Подгоняем размеры вьюхи под размер области просмотра
     */
    protected boolean adjustSurfaceLayoutSize(Camera.Size previewSize, boolean portrait,
                                              int availableWidth, int availableHeight,
                                              Camera.Size mPictureSize) {
        L.i("adjustSurfaceLayoutSize: portrait = " + portrait + "; previewSize.width = " + previewSize.width +
                        "; previewSize.height = " + previewSize.height + "; availableWidth = " + availableWidth +
                        "; availableHeight = " + availableHeight + "; mPictureSize = " + mPictureSize.width + "*" + mPictureSize.height
        );
        L.i("adjustSurfaceLayoutSize:  availableWidth = " + availableWidth +
                        "; availableHeight = " + availableHeight + "; mPictureSize = " + mPictureSize.width + "*" + mPictureSize.height
        );


        float tmpLayoutHeight, tmpLayoutWidth;
        if (portrait) {
            tmpLayoutHeight = previewSize.width;
            tmpLayoutWidth = previewSize.height;
        } else {
            tmpLayoutHeight = previewSize.height;
            tmpLayoutWidth = previewSize.width;
        }

        float factH, factW, fact;
        factH = availableHeight / tmpLayoutHeight;
        factW = availableWidth / tmpLayoutWidth;
        if (mLayoutMode != LayoutMode.FitToParent) {
            // Select smaller factor, because the surface cannot be set to the size larger than display metrics.
            if (factH < factW) {
                fact = factH;
            } else {
                fact = factW;
            }
        } else {
            if (factH < factW) {
                fact = factW;
            } else {
                fact = factH;
            }
        }
        Object l = this.getLayoutParams();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) l;

        L.w(" fact = " + fact);

        int layoutHeight = (int) (tmpLayoutHeight * fact);
        int layoutWidth = (int) (tmpLayoutWidth * fact);
        mViewSize = new Point(layoutWidth, layoutHeight);
        L.w("Установлени размер вьюхи  " + layoutWidth + "*" + layoutHeight);
        boolean layoutChanged;
        if ((layoutWidth != this.getWidth()) || (layoutHeight != this.getHeight())) {
            L.w(" ---------------- 1");
            layoutParams.height = layoutHeight;
            layoutParams.width = layoutWidth;
            L.w("mCenterPosX = " + mCenterPosX);
            if (mCenterPosX >= 0) {
                int topMargin = mCenterPosY - (layoutHeight / 2);
                int leftMargin = mCenterPosX - (layoutWidth / 2);
                L.w(" topMargin = " + topMargin);
                L.w(" leftMargin = " + leftMargin);
                layoutParams.topMargin = topMargin;
                layoutParams.leftMargin = leftMargin;
            }
            this.setLayoutParams(layoutParams); // this will trigger another surfaceChanged invocation.
            layoutChanged = true;
        } else {
            layoutChanged = false;
        }

        return layoutChanged;
    }

    /**
     * @param x X coordinate of center position on the screen. Set to negative value to unset.
     * @param y Y coordinate of center position on the screen.
     */
    public void setCenterPosition(int x, int y) {
        mCenterPosX = x;
        mCenterPosY = y;
    }

    protected void configureCameraParameters(Camera.Parameters cameraParams, boolean portrait) {

        Display display = mActivity.getWindowManager().getDefaultDisplay();
        if (getDeviceDefaultOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            switch (display.getRotation()) {
                case Surface.ROTATION_0: // This is display orientation
                    mAngle = 90; // This is camera orientation
                    break;
                case Surface.ROTATION_90:
                    mAngle = 0;
                    break;
                case Surface.ROTATION_180:
                    mAngle = 270;
                    break;
                case Surface.ROTATION_270:
                    mAngle = 180;
                    break;
                default:
                    mAngle = 90;
                    break;
            }
        } else {
            switch (display.getRotation()) {
                case Surface.ROTATION_0: // This is display orientation
                    L.w("************************* ROTATION_0");
                    mAngle = 0; // This is camera orientation
                    break;
                case Surface.ROTATION_90:
                    L.w("************************* ROTATION_90");
                    mAngle = 270;
                    break;
                case Surface.ROTATION_180:
                    L.w("************************* ROTATION_180");
                    mAngle = 180;
                    break;
                case Surface.ROTATION_270:
                    L.w("************************* ROTATION_270");
                    mAngle = 90;
                    break;
                default:
                    L.w("************************* ROTATION_default");
                    mAngle = 0;
                    break;
            }
        }
        L.w("************************* Установлен поворот " + mAngle);
        mCamera.setDisplayOrientation(mAngle);
        cameraParams.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        cameraParams.setPictureSize(mPictureSize.width, mPictureSize.height);
        L.w("SET PREVIEW SIZE = " + mPreviewSize.width + "*" + mPreviewSize.height);
        L.w("SET CAMERA SIZE = " + mPictureSize.width + "*" + mPictureSize.height);
        mCamera.setParameters(cameraParams);
    }

    public int getDeviceDefaultOrientation() {
        WindowManager windowManager = (WindowManager) MiiskinApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        Configuration config = getResources().getConfiguration();
        int rotation = windowManager.getDefaultDisplay().getRotation();
        if (((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) &&
                config.orientation == Configuration.ORIENTATION_LANDSCAPE)
                || ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) &&
                config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            return Configuration.ORIENTATION_LANDSCAPE;
        } else {
            return Configuration.ORIENTATION_PORTRAIT;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stop();
    }

    public void stop() {
        if (null == mCamera) {
            return;
        }
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    public boolean isPortrait() {
        return (mActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    public void setOneShotPreviewCallback(Camera.PreviewCallback callback) {
        if (null == mCamera) {
            return;
        }
        mCamera.setOneShotPreviewCallback(callback);
    }

    public void setPreviewCallback(Camera.PreviewCallback callback) {
        if (null == mCamera) {
            return;
        }
        mCamera.setPreviewCallback(callback);
    }

    public Camera.Size getPreviewSize() {
        return mPreviewSize;
    }

    public void setOnPreviewReady(PreviewReadyCallback cb) {
        mPreviewReadyCallback = cb;
    }

    public void setShutterCallback(final Camera.ShutterCallback pShutterCallback) {
        this.mShutterCallback = pShutterCallback;
    }

    public void setRawCallback(final Camera.PictureCallback pRawCallback) {
        this.mRawCallback = pRawCallback;
    }

    public void setJpgCallback(final Camera.PictureCallback pJpgCallback) {
        this.mJpgCallback = pJpgCallback;
    }

    public void takePicture() {
        mCamera.takePicture(mShutterCallback, mRawCallback, mJpgCallback);
    }


    public void startPreview() {
        if (mCamera != null)
            mCamera.startPreview();
    }

    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            if (arg0) {
                mCamera.cancelAutoFocus();
            }
        }
    };

    public void doTouchFocus(final Rect tfocusRect) {
        try {
            List<Camera.Area> focusList = new ArrayList<Camera.Area>();
            Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
            focusList.add(focusArea);

            Camera.Parameters param = mCamera.getParameters();
            param.setFocusAreas(focusList);
            param.setMeteringAreas(focusList);
            mCamera.setParameters(param);

            mCamera.autoFocus(myAutoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Unable to autofocus");
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            Rect touchRect = new Rect(
                    (int) (x - 100),
                    (int) (y - 100),
                    (int) (x + 100),
                    (int) (y + 100));


            final Rect targetFocusRect = new Rect(
                    touchRect.left * 2000 / this.getWidth() - 1000,
                    touchRect.top * 2000 / this.getHeight() - 1000,
                    touchRect.right * 2000 / this.getWidth() - 1000,
                    touchRect.bottom * 2000 / this.getHeight() - 1000);

            this.doTouchFocus(targetFocusRect);
            if (mFocusBorderView != null) {
                mFocusBorderView.setHaveTouch(true, touchRect);
                mFocusBorderView.invalidate();

                // Remove the square indicator after 1000 msec
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mFocusBorderView.setHaveTouch(false, new Rect(0, 0, 0, 0));
                        mFocusBorderView.invalidate();
                    }
                }, 1000);
            }

        }

        return false;
    }

    class CameraSizeComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size a, Camera.Size b) {
            return a.width < b.width ? -1 : a.width == b.width ? 0 : 1;
        }
    }

    public Point getRealViewSize() {
        return mViewSize;
    }

    public int getAngle() {
        return mAngle;
    }


}
