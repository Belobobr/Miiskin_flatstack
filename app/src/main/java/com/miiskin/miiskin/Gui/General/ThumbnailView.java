package com.miiskin.miiskin.Gui.General;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.miiskin.miiskin.Storage.ThumbnailManager.ThumbnailManager;

/**
 * Created by Newshka on 30.06.2015.
 */
public class ThumbnailView extends ImageView implements ThumbnailManager.ThumbnailListener{

    private String mThumbnailKey;
    private ThumbnailManager mThumbnailManager;

    public ThumbnailView(Context context) {
        super(context);
        init();
    }

    public ThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThumbnailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mThumbnailManager = ThumbnailManager.getInstance();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mThumbnailManager.registerListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mThumbnailManager.unregisterListener(this);
    }

    @Override
    public void onThumbnailLoaded(String thumbnailKey) {
        boolean matched = this.mThumbnailKey != null && this.mThumbnailKey.equals(thumbnailKey);
        if (matched) {
            ValueAnimator appearingAnimator = ObjectAnimator.ofFloat(this, "alpha", 0, 1);
            appearingAnimator.setDuration(500);
            appearingAnimator.start();
            setImageBitmap(obtainThumbnail());
        }
    }

    private Bitmap obtainThumbnail() {
        if (mThumbnailKey == null) {
            return null;
        }
        Bitmap thumbnail = mThumbnailManager.getThumbnail(mThumbnailKey);
        return thumbnail;
    }

    public String getThumbnailKey() {
        return mThumbnailKey;
    }

    public void setThumbnailKey(String thumbnailKey) {
        this.mThumbnailKey = thumbnailKey;
    }
}
