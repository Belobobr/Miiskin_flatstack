package com.miiskin.miiskin.Gui.General;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.graphics.Paint;

import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 25.06.2015.
 */
public class PointedImageView extends ImageView {

    private float xRelativeCord = -1;
    private float yRelativeCord = -1;
    private Paint mTextPaint;
    private float DEFAULT_POINT_SIZE = 8;

    public static enum PointMode {
        BIG,
        NORMAL
    }

    private PointMode mPointMode = PointMode.BIG;

    public PointedImageView(Context context) {
        super(context);
        init();
    }

    public PointedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(0xFF0000FF);
    }

    public void removePoint() {
        xRelativeCord = -1;
        yRelativeCord = -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getDrawable() != null && xRelativeCord != -1 && yRelativeCord != -1) {
            float mPointerXOnBitmap = ((BitmapDrawable) getDrawable()).getBitmap().getWidth() * xRelativeCord;
            float mPointerYOnBitmap = ((BitmapDrawable) getDrawable()).getBitmap().getHeight() * yRelativeCord;

            float mPointerX = mPointerXOnBitmap;
            float mPointerY = mPointerYOnBitmap;

            Matrix convertMatrix = getImageMatrix();
            float[] touchPoint = new float[]{mPointerX, mPointerY};
            convertMatrix.mapPoints(touchPoint);

            // Draw the pointer
            BitmapDrawable bitmapDrawable = null;
            switch (mPointMode) {
                case BIG :
                    bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.mole_big);
                    break;
                case NORMAL:
                    bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.mole);
                    break;
                default:
                    bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.mole);
            }
            canvas.drawBitmap(bitmapDrawable.getBitmap(),touchPoint[0], touchPoint[1], mTextPaint);

        }
    }

    public void setPoint(float xRelativeCord, float yRelativeCord) {
        this.xRelativeCord = xRelativeCord;
        this.yRelativeCord = yRelativeCord;
    }

    public void setPointMode(PointMode pointMode) {
        mPointMode = pointMode;
    }

    @Override
    public Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putFloat("xRelativeCord", this.xRelativeCord);
        bundle.putFloat("yRelativeCord", this.yRelativeCord);
        bundle.putSerializable("pointMode", this.mPointMode);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.xRelativeCord = bundle.getFloat("xRelativeCord");
            this.yRelativeCord = bundle.getFloat("yRelativeCord");
            mPointMode = (PointMode)bundle.getSerializable("pointMode");
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }
}
