package com.miiskin.miiskin.Gui.CreateSequence;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 24.06.2015.
 */
public class GeneralAreaFragment extends Fragment {

    private ImageView bodyImageView;
    private ImageView bodyImageViewOverlay;

    public static GeneralAreaFragment newInstance() {
        GeneralAreaFragment fragment = new GeneralAreaFragment();
        return fragment;
    }

    public static class BodyPartColors {
        public static final int RIGHT_ARM_COLOR       = 0xFF0000FF;
        public static final int LEFT_ARM_COLOR      = 0xFF00FF00;
    }

    Integer xCoord;
    Integer yCoord;
    int colorTouched;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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



    }

    @Override
    public void onResume() {
        super.onResume();
        bodyImageView.post(new Runnable() {
            @Override
            public void run() {
                final Bitmap bm = decodeSampledBitmapFromResource(getResources(), R.drawable.upper, bodyImageView.getWidth(), bodyImageView.getHeight());
                if (bm!=null) {
                    bodyImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    bodyImageView.setImageBitmap(bm);
                }

                final Bitmap bm2 = decodeSampledBitmapFromResource(getResources(), R.drawable.lower, bodyImageViewOverlay.getWidth(), bodyImageViewOverlay.getHeight());
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
            colorTouched = ((BitmapDrawable)bodyImageViewOverlay.getDrawable()).getBitmap().getPixel(xCoord,yCoord);
            checkTouchZone();
        } catch (IllegalArgumentException e) {
            colorTouched = Color.WHITE; // nothing happens when touching white
        }
    }

    private void checkTouchZone() {
        switch ( colorTouched) {
            case BodyPartColors.LEFT_ARM_COLOR :
                showToast("Left_arm_selected");
                break;
            case BodyPartColors.RIGHT_ARM_COLOR :
                showToast("Right_arm_selected");
                break;
        }
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
