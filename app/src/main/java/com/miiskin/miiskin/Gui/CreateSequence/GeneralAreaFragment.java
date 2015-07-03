package com.miiskin.miiskin.Gui.CreateSequence;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.miiskin.miiskin.Data.BodyHalf;
import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Data.UserInfo;
import com.miiskin.miiskin.Data.UserManager;
import com.miiskin.miiskin.Data.Utils;
import com.miiskin.miiskin.Helpers.BitmapDecoder;
import com.miiskin.miiskin.R;

import java.lang.reflect.Field;

/**
 * Created by Newshka on 24.06.2015.
 */
public class GeneralAreaFragment extends Fragment {

    public final static String TAG = "GENERAL_AREA_FRAGMENT";

    private static final String BODY_PART_COLOR_TOUCHED = "BODY_PART_COLOR_TOUCHED";
    private static final String FRONT_MODE = "FRONT_MODE";

    private ImageView bodyImageView;
    private ImageView bodyImageViewBackground;
    private FloatingActionButton mFloatingActionButton;
    private Button buttonFront;
    private Button buttonRear;
    private boolean frontMode = true;

    public static GeneralAreaFragment newInstance() {
        GeneralAreaFragment fragment = new GeneralAreaFragment();
        return fragment;
    }

    public interface GeneralAreaSelectedListener {
        public void onGeneralAreaSelected(BodyPart bodyPart, BodyHalf bodyHalf);
    }

    private GeneralAreaSelectedListener mListener;

    public static class BodyPartColors {
        public static final int left_hand_male_front  = 0xff127379;
        public static final int left_forearm_male_front = 0xff178b92;
        public static final int left_upper_arm_male_front = 0xff1fa6ae;
        public static final int right_hand_male_front =0xff117046;
        public static final int right_forearm_male_front = 0xff178e59;
        public static final int right_upper_arm_male_front =0xff1fb06f;
        public static final int face_throat_male_front =0xfffffd2f;
        public static final int chest_male_front = 0xffffd824;
        public static final int stomach_male_front = 0xffffb424;
        public static final int groin_male_front = 0xffff8b24;
        public static final int genitals_male_front =0xffee7407;
        public static final int left_thigh_male_front =0xff4381fd;
        public static final int left_shin_male_front = 0xff376dda;
        public static final int left_foot_male_front = 0xff2e57a8;
        public static final int right_thigh_male_front = 0xffc336ec;
        public static final int right_shin_male_front = 0xffaa30cd;
        public static final int right_foot_male_front = 0xff871ea5;
        public static final int not_body = 0xFFFFFFFF;
    }

    Integer xCoord;
    Integer yCoord;
    int prevColorTouched;
    int bodyPartColorTouched = BodyPartColors.not_body;
    BodyPart mBodyPart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            bodyPartColorTouched = savedInstanceState.getInt(BODY_PART_COLOR_TOUCHED);
            frontMode = savedInstanceState.getBoolean(FRONT_MODE);
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
        bodyImageViewBackground = (ImageView)view.findViewById(R.id.bodyImageViewOverlay);


        CreateMoleActivity createMoleActivity = (CreateMoleActivity)getActivity();
        createMoleActivity.mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_clear);
        createMoleActivity.mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mFloatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onGeneralAreaSelected(mBodyPart, frontMode ? BodyHalf.Front : BodyHalf.Rear);
            }
        });
        buttonFront = (Button)view.findViewById(R.id.buttonFront);
        buttonFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bodyPartColorTouched = BodyPartColors.not_body;
                switchToFrontMode();
            }
        });
        buttonRear = (Button)view.findViewById(R.id.buttonRear);
        buttonRear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               switchToRearMode();
            }
        });
    }

    private void switchToFrontMode() {
        switchToFrontMode(false);
    }

    private void switchToFrontMode(boolean withoutLoadingImage) {
        buttonRear.setSelected(false);
        buttonFront.setSelected(true);
        frontMode = true;
        if (!withoutLoadingImage) {
            checkTouchZone();
            loadBackgroundImage(UserManager.getInstance().getUserGender(), frontMode);
        }
    }

    private void switchToRearMode() {
        switchToRearMode(false);
    }

    private void switchToRearMode(boolean withoutLoadingImage) {
        buttonFront.setSelected(false);
        buttonRear.setSelected(true);
        frontMode = false;
        if (!withoutLoadingImage) {
            checkTouchZone();
            loadBackgroundImage(UserManager.getInstance().getUserGender(), frontMode);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BODY_PART_COLOR_TOUCHED, bodyPartColorTouched);
        outState.putBoolean(FRONT_MODE, frontMode);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (frontMode) {
            switchToFrontMode(true);
        } else {
            switchToRearMode(true);
        }
        bodyImageView.post(new Runnable() {
            @Override
            public void run() {
                checkTouchZone();
                loadBackgroundImage(UserManager.getInstance().getUserGender(), frontMode);
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
        xCoord = Integer.valueOf((int)touchPoint[0]);
        yCoord = Integer.valueOf((int)touchPoint[1]);

        try {
            prevColorTouched = bodyPartColorTouched;
            bodyPartColorTouched = ((BitmapDrawable) bodyImageViewBackground.getDrawable()).getBitmap().getPixel(xCoord,yCoord);
        } catch (IllegalArgumentException e) {
            prevColorTouched = bodyPartColorTouched;
            bodyPartColorTouched = BodyPartColors.not_body; // nothing happens when touching white
        }

        if (prevColorTouched != bodyPartColorTouched) {
            checkTouchZone();
        }
    }

    private void checkTouchZone() {
        switch (bodyPartColorTouched) {
            case BodyPartColors.left_hand_male_front :
                mBodyPart = BodyPart.LeftHand;
                break;
            case BodyPartColors.right_hand_male_front :
                mBodyPart = BodyPart.RightHand;
                break;
            case BodyPartColors.left_forearm_male_front:
                mBodyPart = BodyPart.LeftForeArm;
                break;
            case BodyPartColors.left_upper_arm_male_front:
                mBodyPart = BodyPart.LeftUpperArm;
                break;
            case BodyPartColors.right_forearm_male_front:
                mBodyPart = BodyPart.RightForeArm;
                break;
            case BodyPartColors.right_upper_arm_male_front:
                mBodyPart = BodyPart.RightUpperArm;
                break;
            case BodyPartColors.face_throat_male_front:
                mBodyPart = BodyPart.FaceThroat;
                break;
            case BodyPartColors.chest_male_front:
                mBodyPart = BodyPart.Chest;
                break;
            case BodyPartColors.stomach_male_front:
                mBodyPart = BodyPart.Stomach;
                break;
            case BodyPartColors.groin_male_front:
                mBodyPart = BodyPart.Groin;
                break;
            case BodyPartColors.genitals_male_front:
                mBodyPart = BodyPart.Genitals;
                break;
            case BodyPartColors.left_thigh_male_front:
                mBodyPart = BodyPart.LeftThigh;
                break;
            case BodyPartColors.right_thigh_male_front:
                mBodyPart = BodyPart.RightThigh;
                break;
            case BodyPartColors.left_shin_male_front:
                mBodyPart = BodyPart.LeftShin;
                break;
            case BodyPartColors.right_shin_male_front:
                mBodyPart = BodyPart.RightShin;
                break;
            case BodyPartColors.left_foot_male_front:
                mBodyPart = BodyPart.LeftFoot;
                break;
            case BodyPartColors.right_foot_male_front:
                mBodyPart = BodyPart.RightFoot;
                break;
            case BodyPartColors.not_body :
            default:
                mBodyPart = BodyPart.Main;
                break;
        }
        switchToBodyPartImage(mBodyPart);
    }

    private void loadBackgroundImage(String gender, boolean frontMode) {
        String resourceName = (gender.equals(UserInfo.MALE) ? "male" : "female") + "_color_map_" + (frontMode ? "front" : "rear");

        Resources r = getResources();
        int drawableResourceId = r.getIdentifier(resourceName, "drawable", "com.miiskin.miiskin");

        final Bitmap bodyBitmapBackground = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), drawableResourceId, bodyImageViewBackground.getWidth(), bodyImageViewBackground.getHeight());
        if (bodyBitmapBackground!=null) {
            bodyImageViewBackground.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bodyImageViewBackground.setImageBitmap(bodyBitmapBackground);

            bodyImageView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent mev) {
                    DecodeActionDownEvent(v, mev, bodyBitmapBackground);
                    return false;
                }

            });
        } else {
            bodyImageViewBackground.setImageBitmap(null);
            bodyImageView.setOnTouchListener(null);
        }
    }

    private void switchToBodyPartImage(BodyPart bodyPart) {
        String gender = UserManager.getInstance().getUserGender();
        if (bodyPart.equals(BodyPart.Main)) {
            String resourceName = (gender.equals(UserInfo.MALE) ? "male" : "female")  + (frontMode ? "_front" : "_rear");
            Resources r = getResources();
            int drawableResourceId = r.getIdentifier(resourceName, "drawable", "com.miiskin.miiskin");
            loadBodyImageView(drawableResourceId);

            mFloatingActionButton.setEnabled(false);
            mFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab_semitrasparent));
            CreateMoleActivity createMoleActivity = (CreateMoleActivity)getActivity();
            createMoleActivity.mActionBarToolbar.setTitle(R.string.please_select_the_general_area);
            return;
        }
        String bodyPartString = null;
        bodyPartString = Utils.bodyPartName(bodyPart);

        String resourceName = bodyPartString + (gender.equals(UserInfo.MALE) ? "_male" : "_female") + (frontMode ? "_front" : "_rear");
        Resources r = getResources();
        int drawableResourceId = r.getIdentifier(resourceName, "drawable", "com.miiskin.miiskin");
        loadBodyImageView(drawableResourceId);

        mFloatingActionButton.setEnabled(true);
        mFloatingActionButton.setBackgroundTintList(getResources().getColorStateList(R.color.home_fab));
        CreateMoleActivity createMoleActivity = (CreateMoleActivity)getActivity();
        createMoleActivity.mActionBarToolbar.setTitle(getString(R.string.body_part_selected, getString(bodyPart.getResourceIdDescription())));
    }

    private void loadBodyImageView(int resId) {
        final Bitmap bm = BitmapDecoder.decodeSampledBitmapFromResource(getResources(), resId, bodyImageView.getWidth(), bodyImageView.getHeight());
        if (bm!=null) {
            bodyImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bodyImageView.setImageBitmap(bm);
        } else {
            bodyImageView.setImageBitmap(null);
        }
    }



}
