package com.miiskin.miiskin.Gui.CreateSequence;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Helpers.BitmapDecoder;
import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 24.06.2015.
 */
public class GeneralAreaFragment extends Fragment {

    public final static String TAG = "GENERAL_AREA_FRAGMENT";

    private static final String BODY_PART_COLOR_TOUCHED = "BODY_PART_COLOR_TOUCHED";

    private ImageView bodyImageView;
    private ImageView bodyImageViewOverlay;
    private FloatingActionButton mFloatingActionButton;

    public static GeneralAreaFragment newInstance() {
        GeneralAreaFragment fragment = new GeneralAreaFragment();
        return fragment;
    }

    public interface GeneralAreaSelectedListener {
        public void onGeneralAreaSelected(BodyPart bodyPart);
    }

    private GeneralAreaSelectedListener mListener;

    public static class BodyPartColors {
        public static final int NOT_BODY_COLOR = 0xFFFFFFFF;
        public static final int RIGHT_ARM_COLOR       = 0xFF0000FF;
        public static final int LEFT_ARM_COLOR      = 0xFF00FF00;
    }

    Integer xCoord;
    Integer yCoord;
    int prevColorTouched;
    int bodyPartColorTouched = BodyPartColors.NOT_BODY_COLOR;
    BodyPart mBodyPart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            bodyPartColorTouched = savedInstanceState.getInt(BODY_PART_COLOR_TOUCHED);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        super.onAttach(activity);
        try {
            mListener = (GeneralAreaSelectedListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_general_area, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bodyImageView = (ImageView)view.findViewById(R.id.bodyImageView);
        bodyImageViewOverlay = (ImageView)view.findViewById(R.id.bodyImageViewOverlay);


        CreateSequenceActivity createSequenceActivity = (CreateSequenceActivity)getActivity();
        createSequenceActivity.mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_clear);
        createSequenceActivity.mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGeneralAreaSelected(mBodyPart);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BODY_PART_COLOR_TOUCHED, bodyPartColorTouched);
    }

    @Override
    public void onResume() {
        super.onResume();
        bodyImageView.post(new Runnable() {
            @Override
            public void run() {
                checkTouchZone();

                final Bitmap bm2 = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), R.drawable.lower, bodyImageViewOverlay.getWidth(), bodyImageViewOverlay.getHeight());
                if (bm2!=null) {
                    bodyImageViewOverlay.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    bodyImageViewOverlay.setImageBitmap(bm2);

                    bodyImageView.setOnTouchListener(new View.OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent mev) {
                            DecodeActionDownEvent(v, mev, bm2);
                            return false;
                        }

                    });
                }
            }
        });
    }

    private void loadBodyImageView(int resId) {
        final Bitmap bm = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), resId, bodyImageView.getWidth(), bodyImageView.getHeight());
        if (bm!=null) {
            bodyImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bodyImageView.setImageBitmap(bm);
        }
    }

    private void DecodeActionDownEvent(View v, MotionEvent ev, Bitmap bm2)
    {
        //Something a little bit difficult
        Matrix inverse = new Matrix();
        bodyImageView.getImageMatrix().invert(inverse);
        float[] touchPoint = new float[] {ev.getX(), ev.getY()};
        inverse.mapPoints(touchPoint);
        xCoord = Integer.valueOf((int)touchPoint[0]);
        yCoord = Integer.valueOf((int)touchPoint[1]);

        try {
            prevColorTouched = bodyPartColorTouched;
            bodyPartColorTouched = ((BitmapDrawable)bodyImageViewOverlay.getDrawable()).getBitmap().getPixel(xCoord,yCoord);
        } catch (IllegalArgumentException e) {
            prevColorTouched = bodyPartColorTouched;
            bodyPartColorTouched = BodyPartColors.NOT_BODY_COLOR; // nothing happens when touching white
        }

        if (prevColorTouched != bodyPartColorTouched) {
            checkTouchZone();
        }
    }

    private void checkTouchZone() {
        CreateSequenceActivity createSequenceActivity = (CreateSequenceActivity)getActivity();
        switch (bodyPartColorTouched) {
            case BodyPartColors.LEFT_ARM_COLOR :
                mBodyPart = BodyPart.LeftUpperArm;
                loadBodyImageView(R.drawable.left_arm_selected);
                mFloatingActionButton.setEnabled(true);
                mFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab));
                createSequenceActivity.mActionBarToolbar.setTitle(R.string.left_upper_arm_selected);
                break;
            case BodyPartColors.RIGHT_ARM_COLOR :
                mBodyPart = BodyPart.RightUpperArm;
                loadBodyImageView(R.drawable.right_arm_selected);
                mFloatingActionButton.setEnabled(true);
                mFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab));
                createSequenceActivity.mActionBarToolbar.setTitle(R.string.right_upper_arm_selected);
                break;
            case BodyPartColors.NOT_BODY_COLOR :
            default:
                loadBodyImageView(R.drawable.no_body_part_selected);
                mFloatingActionButton.setEnabled(false);
                mFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab_semitrasparent));
                createSequenceActivity.mActionBarToolbar.setTitle(R.string.please_select_the_general_area);
                break;
        }
    }
}
