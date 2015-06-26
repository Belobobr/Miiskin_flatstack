package com.miiskin.miiskin.Gui.Camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.miiskin.miiskin.Data.L;
import com.miiskin.miiskin.R;


/**
 * Рамка которая рисуется на вьехе камеры.
 * Рамка соответствует по пропорциям А4.
 * Created by Kalashnikov S.A. on 17.11.2014.
 */
public class DocCameraBorder extends FrameLayout {

    private int mViewWidth;
    private int mViewHeight;
    private Paint mPaint;
    private Rect mBorderRect;

    public DocCameraBorder(Context context) {
        super(context);
        init(context);
    }

    public DocCameraBorder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DocCameraBorder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(context.getResources().getColor(R.color.blueye));

    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        L.i("onSizeChanged = " + mViewWidth + "*" + mViewHeight);
    }

    public Rect getBorderRect() {
        return mBorderRect;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //рисуем рамку для А4
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        float left, top, right, bottom, borderWidth, borderHeight;
        if (mViewWidth > mViewHeight) {
            //ланшафт
            borderHeight = mViewHeight - 20;
            borderWidth = (float) (borderHeight / 1.41);
            left = (mViewWidth - borderWidth) / 2;
            top = 10;
            right = left + borderWidth;
            bottom = 10 + borderHeight;
        } else {
            //портрет
            //определяем по какой стороне будем расчитывать рамку
            borderWidth = mViewWidth - 20;
            borderHeight = mViewHeight - 20;
            float centerX = mViewWidth / 2;
            float centerY = mViewHeight / 2;
            if ((borderWidth * 1.41) > borderHeight) {
                borderWidth = (float) (borderHeight / 1.41);
            } else {
                borderHeight = (float) (borderWidth * 1.41);
            }
            top = centerY - (borderHeight / 2);
            bottom = centerY + (borderHeight / 2);
            left = centerX - (borderWidth / 2);
            right = centerX + (borderWidth / 2);
        }
        L.w("border = " + borderWidth + "*" + borderHeight);
        L.w("rect: left =" + left + "; top = " + top + "; right = " + right + "; bottom = " + bottom);

        mBorderRect = new Rect((int) left, (int) top, (int) right, (int) bottom);
        canvas.drawRect(left, top, right, bottom, mPaint);

    }

}
