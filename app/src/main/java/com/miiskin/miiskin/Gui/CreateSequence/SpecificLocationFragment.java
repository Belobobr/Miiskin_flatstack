package com.miiskin.miiskin.Gui.CreateSequence;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
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

import com.miiskin.miiskin.Data.BodyHalf;
import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Data.UserInfo;
import com.miiskin.miiskin.Data.UserManager;
import com.miiskin.miiskin.Data.Utils;
import com.miiskin.miiskin.Gui.General.PointedImageView;
import com.miiskin.miiskin.Helpers.BitmapDecoder;
import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 24.06.2015.
 */
public class SpecificLocationFragment extends Fragment {

    public final static String TAG = "SPECIFIC_LOCATION_FRAGMENT";

    private static final String BODY_PART_COLOR_TOUCHED = "BODY_PART_COLOR_TOUCHED";
    private static final String BODY_PART_X_CORD = "BODY_PART_X_CORD";
    private static final String BODY_PART_Y_CORD = "BODY_PART_Y_CORD";
    private static final String CHOSEN_BODY_PART = "CHOSEN_BODY_PART";
    private static final String CHOSEN_BODY_HALF = "CHOSEN_BODY_HALF";

    public static class BodyPartColors {
        public static final int NOT_BODY_COLOR = 0xFFedecec;
        public static final int BODY_COLOR = 0xFF666666;
    }

    int prevColorTouched;
    int bodyPartColorTouched = BodyPartColors.NOT_BODY_COLOR;
    BodyPart mBodyPart;
    BodyHalf mBodyHalf;
    float bodyPartRelativePointX;
    float bodyPartRelativePointY;


    private PointedImageView bodyImageView;
    private ImageView bodyImageViewBackground;
    private FloatingActionButton mFloatingActionButton;


    public static SpecificLocationFragment newInstance(BodyPart bodyPart, BodyHalf bodyHalf) {
        SpecificLocationFragment fragment = new SpecificLocationFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(CHOSEN_BODY_PART, bodyPart);
        arguments.putSerializable(CHOSEN_BODY_HALF, bodyHalf);
        fragment.setArguments(arguments);
        return fragment;
    }

    public interface SpecificLocationSelectedListener {
        public void onSpecificLocationSelected(float bodyPartRelativePointX, float bodyPartRelativePointY);
    }

    private SpecificLocationSelectedListener mListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mBodyPart = (BodyPart)arguments.getSerializable(CHOSEN_BODY_PART);
            mBodyHalf = (BodyHalf)arguments.getSerializable(CHOSEN_BODY_HALF);
        }
        if (savedInstanceState != null) {
            bodyPartRelativePointX = savedInstanceState.getFloat(BODY_PART_X_CORD);
            bodyPartRelativePointY = savedInstanceState.getFloat(BODY_PART_Y_CORD);
            bodyPartColorTouched = savedInstanceState.getInt(BODY_PART_COLOR_TOUCHED);
            mBodyPart = (BodyPart)savedInstanceState.getSerializable(CHOSEN_BODY_PART);
            mBodyHalf = (BodyHalf)savedInstanceState.getSerializable(CHOSEN_BODY_HALF);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BODY_PART_COLOR_TOUCHED, bodyPartColorTouched);
        outState.putFloat(BODY_PART_X_CORD, bodyPartRelativePointX);
        outState.putFloat(BODY_PART_Y_CORD, bodyPartRelativePointY);
        outState.putSerializable(CHOSEN_BODY_PART, mBodyPart);
        outState.putSerializable(CHOSEN_BODY_HALF, mBodyHalf);
    }

    @Override
    public void onResume() {
        super.onResume();
        bodyImageView.post(new Runnable() {
            @Override
            public void run() {
                checkTouchZone();
                loadSpecificImages();
            }
        });
    }

    private void loadSpecificImages() {
        loadBodyImageView();
        loadBackgroundImageView();
    }

    private void loadBodyImageView() {
        int drawableResourceId = Utils.getImageResourceId(getActivity(), mBodyPart, UserManager.getInstance().getUserGender(), mBodyHalf, false);
        final Bitmap bm = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), drawableResourceId, bodyImageView.getWidth(), bodyImageView.getHeight());
        if (bm!=null) {
            bodyImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bodyImageView.setImageBitmap(bm);
        }
    }

    private void loadBackgroundImageView() {
        int drawableResourceId = Utils.getImageResourceId(getActivity(), mBodyPart, UserManager.getInstance().getUserGender(), mBodyHalf, true);
        final Bitmap bm2 = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), drawableResourceId, bodyImageViewBackground.getWidth(), bodyImageViewBackground.getHeight());
        if (bm2!=null) {
            bodyImageViewBackground.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bodyImageViewBackground.setImageBitmap(bm2);

            bodyImageView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent mev) {
                    DecodeActionDownEvent(v, mev, bm2);
                    return false;
                }

            });
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        super.onAttach(activity);
        try {
            mListener = (SpecificLocationSelectedListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_specific_location, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bodyImageView = (PointedImageView)view.findViewById(R.id.bodyImageView);
        bodyImageViewBackground = (ImageView)view.findViewById(R.id.bodyImageViewOverlay);

        final CreateMoleActivity createMoleActivity = (CreateMoleActivity)getActivity();
        createMoleActivity.mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        createMoleActivity.mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMoleActivity.getFragmentManager().popBackStack();
            }
        });

        mFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSpecificLocationSelected(bodyPartRelativePointX, bodyPartRelativePointY);
            }
        });
    }

    private void DecodeActionDownEvent(View v, MotionEvent ev, Bitmap bm2)
    {
        //Something a little bit difficult
        Matrix inverse = new Matrix();
        bodyImageView.getImageMatrix().invert(inverse);
        float[] touchPoint = new float[] {ev.getX(), ev.getY()};
        inverse.mapPoints(touchPoint);
        int xCord = Integer.valueOf((int)touchPoint[0]);
        int yCord = Integer.valueOf((int)touchPoint[1]);
        int bitmapWidth = ((BitmapDrawable)bodyImageView.getDrawable()).getBitmap().getWidth();
        int bitmapHeight = ((BitmapDrawable)bodyImageView.getDrawable()).getBitmap().getHeight();

        //constrain position to image frame
        if (xCord < 0) {
            xCord = 0;
        } else if (xCord > bitmapWidth) {
            xCord = bitmapWidth;
        }

        if (yCord < 0) {
            yCord = 0;
        } else if (yCord > bitmapHeight) {
            yCord = bitmapHeight;
        }

        bodyPartRelativePointX = (float)xCord / bitmapWidth ;
        bodyPartRelativePointY = (float)yCord / bitmapHeight ;

        try {
            prevColorTouched = bodyPartColorTouched;
            bodyPartColorTouched = ((BitmapDrawable) bodyImageViewBackground.getDrawable()).getBitmap().getPixel(xCord,yCord);
        } catch (IllegalArgumentException e) {
            prevColorTouched = bodyPartColorTouched;
            bodyPartColorTouched = BodyPartColors.NOT_BODY_COLOR; // nothing happens when touching white
        }

        int bodyPartColor = checkTouchZone();
        if (bodyPartColor == BodyPartColors.BODY_COLOR) {
            bodyImageView.setPoint(bodyPartRelativePointX, bodyPartRelativePointY);
            bodyImageView.invalidate();
        } else {
            bodyImageView.removePoint();
            bodyImageView.invalidate();
        }
    }

    private int checkTouchZone() {
        CreateMoleActivity createMoleActivity = (CreateMoleActivity)getActivity();
        switch (bodyPartColorTouched) {
            case BodyPartColors.BODY_COLOR :
                mFloatingActionButton.setEnabled(true);
                mFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab));
                createMoleActivity.mActionBarToolbar.setTitle(R.string.specific_location_selected);
                return BodyPartColors.BODY_COLOR;
            case BodyPartColors.NOT_BODY_COLOR :
            default:
                mFloatingActionButton.setEnabled(false);
                mFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab_semitrasparent));
                createMoleActivity.mActionBarToolbar.setTitle(R.string.select_specific_area);
                return BodyPartColors.NOT_BODY_COLOR;
        }
    }


}
