package com.miiskin.miiskin.Gui.ViewSequence;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.miiskin.miiskin.Data.Paths;
import com.miiskin.miiskin.Data.SequenceData;
import com.miiskin.miiskin.Gui.Camera.CameraActivity;
import com.miiskin.miiskin.Gui.SendToDoctor.SendToDoctorActivity;
import com.miiskin.miiskin.Helpers.BitmapDecoder;
import com.miiskin.miiskin.MiiskinApplication;
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
    RelativeLayout mMainLayout;
    PagerContainer mContainer;
    private float mYDoctorInitialPosition;
    private float mYPhotoInitialPosition;
    private boolean mButtonsVisible = false;
    LayoutInflater mLayoutInflater;
    ImageView mFullScreenImagePreview;
    FrameLayout mFullScreenMode;
    LinearLayout mPreviewMode;
    LinearLayout mBottomRightPanel;
    RelativeLayout mInfoPanel;
    private ViewPager mPager;
    private AnimatorSet mAnimPhotoAndDoctorHidding;
    private AnimatorSet mAnimPhotoAndDoctorAppearing;
    private AnimatorSet mSwitchToFullScreenAnimator;
    private AnimatorSet mSwitchToNotFullScreenModeAnimator;


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
        mLayoutInflater = (LayoutInflater) MiiskinApplication.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (getArguments() != null) {
            mSequenceData = (SequenceData)getArguments().getSerializable(EXTRA_SEQUENCE_DATA);
        }
    }

    private void switchToNotFullScreenMode() {
        mBottomRightPanel.setVisibility(View.VISIBLE);
        mPreviewMode.setVisibility(View.VISIBLE);
        final ViewSequenceActivity viewSequenceActivity = (ViewSequenceActivity)getActivity();
        Toolbar toolbar = viewSequenceActivity.mActionBarToolbar;
        toolbar.setVisibility(View.VISIBLE);

        ValueAnimator showActionBarAnimator = ObjectAnimator.ofFloat(toolbar, "translationY", -toolbar.getHeight(), 0);
        ValueAnimator appearInfoPanelAnimator = ObjectAnimator.ofFloat(mInfoPanel, "alpha", 0, 1);
        ValueAnimator floatingActionButtonAnimator = ObjectAnimator.ofFloat(mFloatingActionButton, "translationY", mInfoPanel.getHeight(), 0);
        floatingActionButtonAnimator.setInterpolator(new OvershootInterpolator(4));

        mSwitchToNotFullScreenModeAnimator = new AnimatorSet();
        mSwitchToNotFullScreenModeAnimator.playTogether(showActionBarAnimator, appearInfoPanelAnimator, floatingActionButtonAnimator);
        mSwitchToNotFullScreenModeAnimator.setDuration(500);
        mSwitchToNotFullScreenModeAnimator.start();

        mFullScreenMode.setVisibility(View.GONE);
    }

    private void switchToFullScreenMode() {
        mFullScreenMode.setVisibility(View.VISIBLE);

        mPreviewMode.setVisibility(View.GONE);
        final ViewSequenceActivity viewSequenceActivity = (ViewSequenceActivity)getActivity();
        final Toolbar toolbar = viewSequenceActivity.mActionBarToolbar;

        ValueAnimator hideActionBarAnimator = ObjectAnimator.ofFloat(toolbar, "translationY", 0, -toolbar.getHeight());
        ValueAnimator disappearInfoPanelAnimator = ObjectAnimator.ofFloat(mInfoPanel, "alpha", 1, 0);
        ValueAnimator floatingActionButtonAnimator = ObjectAnimator.ofFloat(mFloatingActionButton, "translationY", 0, mInfoPanel.getHeight());

        mSwitchToFullScreenAnimator = new AnimatorSet();
        mSwitchToFullScreenAnimator.setDuration(500);
        mSwitchToFullScreenAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                toolbar.setVisibility(View.INVISIBLE);
                mBottomRightPanel.setVisibility(View.INVISIBLE);
            }

        });
        mSwitchToFullScreenAnimator.playTogether(hideActionBarAnimator, disappearInfoPanelAnimator, floatingActionButtonAnimator);
        mSwitchToFullScreenAnimator.start();

        final String fileName = Paths.getAbsoluteDirForSequence(mSequenceData.mId) + "/" + ( mPager.getCurrentItem() + 1) + ".png";
        mFullScreenImagePreview.post(new Runnable() {
            @Override
            public void run() {

                final Bitmap bitmap = BitmapDecoder.decodeBitmapFromFile(fileName, mFullScreenImagePreview.getWidth(), mFullScreenImagePreview.getHeight());
                if (bitmap!=null) {
                    mFullScreenImagePreview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    mFullScreenImagePreview.setImageBitmap(bitmap);
                }
            }
        });
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
        mMainLayout = (RelativeLayout)view.findViewById(R.id.coordinatorLayout);

        mContainer = (PagerContainer) view.findViewById(R.id.pager_container);
        mFullScreenImagePreview = (ImageView)view.findViewById(R.id.full_screen_image_preview);
        mFullScreenImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fullScreenModeAnimationRunning()) {
                    switchToNotFullScreenMode();
                }
            }
        });
        mFullScreenMode = (FrameLayout)view.findViewById(R.id.full_screen_mode_layout);
        mPreviewMode = (LinearLayout)view.findViewById(R.id.preview_mode);
        mBottomRightPanel = (LinearLayout)view.findViewById(R.id.bottomRightPanel);
        mInfoPanel = (RelativeLayout)view.findViewById(R.id.infoPanel);

        mPager = mContainer.getViewPager();
        PagerAdapter adapter = new MyPagerAdapter();
        mPager.setAdapter(adapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mButtonsVisible) {
                    hideButtons();
                }
            }
        });
        //Necessary or the pager will only have one extra page to show
        // make this at least however many pages you can see
        mPager.setOffscreenPageLimit(3);
        //A little space between pages
        mPager.setPageMargin(15);

        //If hardware acceleration is enabled, you should also remove
        // clipping on the pager for its children.
        mPager.setClipChildren(false);
    }

    private boolean fullScreenModeAnimationRunning() {
        return (mSwitchToNotFullScreenModeAnimator != null  && mSwitchToNotFullScreenModeAnimator.isRunning())   ||
                (mSwitchToFullScreenAnimator != null && mSwitchToFullScreenAnimator.isRunning());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_sequence, container, false);
    }

    private void sendToDoctor() {
        Intent intent = new Intent(getActivity(), SendToDoctorActivity.class);
        startActivity(intent);
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

    private boolean buttonsAnimationRunning() {
        return (mAnimPhotoAndDoctorAppearing != null && mAnimPhotoAndDoctorAppearing.isRunning()) ||
                (mAnimPhotoAndDoctorHidding != null && mAnimPhotoAndDoctorHidding.isRunning());
    }

    private void showButtons() {
        if (!buttonsAnimationRunning()) {
            float centerFabY = mFloatingActionButton.getY() + mFloatingActionButton.getHeight() / 2;

            float yDoctorFrom = centerFabY - mSendToDoctor.getHeight() / 2;
            float yPhotoFrom = centerFabY - mTakePhoto.getHeight() / 2;

            ValueAnimator doctorTransitionAnimator = ObjectAnimator.ofFloat(mSendToDoctor, "y", yDoctorFrom, mYDoctorInitialPosition);
            ValueAnimator photoTransitionAnimator = ObjectAnimator.ofFloat(mTakePhoto, "y", yPhotoFrom, mYPhotoInitialPosition);

            mAnimPhotoAndDoctorAppearing = new AnimatorSet();
            mAnimPhotoAndDoctorAppearing.playTogether(doctorTransitionAnimator, photoTransitionAnimator);
            mAnimPhotoAndDoctorAppearing.addListener(new AnimatorListenerAdapter() {
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
            mAnimPhotoAndDoctorAppearing.setDuration(250);
            mAnimPhotoAndDoctorAppearing.setInterpolator(new OvershootInterpolator());
            mAnimPhotoAndDoctorAppearing.start();
        }
    }

    private void hideButtons() {
        if (!buttonsAnimationRunning()) {
            float centerFabY = mFloatingActionButton.getY() + mFloatingActionButton.getHeight() / 2;

            float yDoctorTo = centerFabY - mSendToDoctor.getHeight() / 2;
            float yPhotoTo = centerFabY - mTakePhoto.getHeight() / 2;

            ValueAnimator doctorTransitionAnimator = ObjectAnimator.ofFloat(mSendToDoctor, "y", mYDoctorInitialPosition, yDoctorTo);
            ValueAnimator photoTransitionAnimator = ObjectAnimator.ofFloat(mTakePhoto, "y", mYPhotoInitialPosition, yPhotoTo);

            mAnimPhotoAndDoctorHidding = new AnimatorSet();
            mAnimPhotoAndDoctorHidding.playTogether(doctorTransitionAnimator, photoTransitionAnimator);
            mAnimPhotoAndDoctorHidding.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    mSendToDoctor.setVisibility(View.INVISIBLE);
                    mTakePhoto.setVisibility(View.INVISIBLE);
                    mButtonsVisible = false;
                    mFloatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_add));
                }
            });
            mAnimPhotoAndDoctorHidding.setDuration(250);
            mAnimPhotoAndDoctorHidding.start();
        }
    }

    //Nothing special about this adapter, just throwing up colored views for demo
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            FrameLayout frameLayout = (FrameLayout)mLayoutInflater.inflate(R.layout.layout_mole_pager, container, false);
            frameLayout.setBackgroundColor(Color.argb(255, position * 50, position * 10, position * 50));
            frameLayout.setClipChildren(true);

            final ImageView imageView = (ImageView)frameLayout.findViewById(R.id.mole_photo);
            final String fileName = Paths.getAbsoluteDirForSequence(mSequenceData.mId) + "/" + (position + 1) + ".png";
            imageView.post(new Runnable() {
                @Override
                public void run() {

                    final Bitmap bitmap = BitmapDecoder.decodeBitmapFromFile(fileName, imageView.getWidth(), imageView.getHeight());
                    if (bitmap!=null) {
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setImageBitmap(bitmap);
                    }
                }
            });
//
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!fullScreenModeAnimationRunning()) {
                        switchToFullScreenMode();
                    }
                }
            });
            container.addView(frameLayout);
            return frameLayout;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            String absoluteDir =  Paths.getAbsoluteDirForSequence(mSequenceData.mId);
            File file = new File(absoluteDir);
            if (file.listFiles() != null) {
               return file.listFiles().length;
            };
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }
}
