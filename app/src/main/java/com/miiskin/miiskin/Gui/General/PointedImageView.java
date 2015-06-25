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

/**
 * Created by Newshka on 25.06.2015.
 */
public class PointedImageView extends ImageView {

    private float xRelativeCord = -1;
    private float yRelativeCord = -1;
    private Paint mTextPaint;
    private float mPointerSize;
    private float DEFAULT_POINT_SIZE = 8;

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
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_POINT_SIZE, r.getDisplayMetrics());
        mPointerSize = px;
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
            canvas.drawCircle(touchPoint[0], touchPoint[1], mPointerSize, mTextPaint);
        }
    }

    public void setPoint(float xRelativeCord, float yRelativeCord) {
        this.xRelativeCord = xRelativeCord;
        this.yRelativeCord = yRelativeCord;
    }

    public void setPointerSize(float pointerSize) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pointerSize, r.getDisplayMetrics());
        mPointerSize = px;
    }

    @Override
    public Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putFloat("xRelativeCord", this.xRelativeCord);
        bundle.putFloat("yRelativeCord", this.yRelativeCord);
        bundle.putFloat("pointerSize", this.mPointerSize);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.xRelativeCord = bundle.getFloat("xRelativeCord");
            this.yRelativeCord = bundle.getFloat("yRelativeCord");
            this.mPointerSize = bundle.getFloat("mPointerSize");
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }
}
