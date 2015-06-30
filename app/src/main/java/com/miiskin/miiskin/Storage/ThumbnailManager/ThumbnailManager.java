package com.miiskin.miiskin.Storage.ThumbnailManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;

import com.miiskin.miiskin.Helpers.BitmapDecoder;

/**
 * Created by Newshka on 30.06.2015.
 */
public class ThumbnailManager {

    Handler mHandler;
    int cacheSize = 4 * 1024 * 1024; // 4MiB
    LruCache bitmapCache = new LruCache(cacheSize) {
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();

        }};

    ExecutorService mThreadExecutor = Executors.newFixedThreadPool(3);

    public enum ThumbnailMode {
        LARGE,
        NORMAL
    }

    private static ThumbnailManager instance;
    private ThumbnailManager() {
        mHandler = new Handler();
    }

    public interface ThumbnailListener {
        public void onThumbnailLoaded(String thumbnailKey);
    }

    private List<ThumbnailListener> mThumbnailListeners = new ArrayList<ThumbnailListener>();

    public static ThumbnailManager getInstance() {
        if (instance == null ) {
            instance = new ThumbnailManager();
        }
        return instance;
    }

    public void registerListener(ThumbnailListener thumbnailListener) {
        mThumbnailListeners.add(thumbnailListener);
    }

    public void unregisterListener(ThumbnailListener thumbnailListener) {
        mThumbnailListeners.remove(thumbnailListener);
    }

    public void notifyDataChangeListener(String thumbnailKey) {
        for (ThumbnailListener dataChangeListener : mThumbnailListeners) {
            dataChangeListener.onThumbnailLoaded(thumbnailKey);
        }
    }

    public void requestThumbnail(final ThumbnailMode thumbnailMode, final String imagePath, final int thumbnailWidth, final int thumbnailHeight) {
        mThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final String thumbnailKey = getThumbnailKey(thumbnailMode, imagePath);
                if (getThumbnail(thumbnailKey) == null) {
                    Bitmap bitmap = BitmapDecoder.decodeBitmapFromFile(imagePath, thumbnailWidth, thumbnailHeight);
                    bitmapCache.put(thumbnailKey, bitmap);
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataChangeListener(thumbnailKey);
                    }
                });
            }
        });
    }

    public String getThumbnailKey(ThumbnailMode thumbnailMode, String imagePath) {
        return thumbnailMode.toString() + imagePath;
    }

    public Bitmap getThumbnail(ThumbnailMode thumbnailMode, String imagePath) {
        return (Bitmap)bitmapCache.get(getThumbnailKey(thumbnailMode, imagePath));
    }

    public Bitmap getThumbnail(String thumbnailKey) {
        return (Bitmap)bitmapCache.get(thumbnailKey);
    }
}
