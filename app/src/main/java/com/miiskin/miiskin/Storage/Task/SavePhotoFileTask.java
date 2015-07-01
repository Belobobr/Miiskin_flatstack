package com.miiskin.miiskin.Storage.Task;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.miiskin.miiskin.Data.L;
import com.miiskin.miiskin.Data.SavedPhotoInfo;
import com.miiskin.miiskin.MiiskinApplication;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MolePicture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by Newshka on 26.06.2015.
 */
public class SavePhotoFileTask extends Task {

    /**
     * Фонованая таска для сохранения фото документа.
     *
     * pBytes        исходное изображение
     * pDisplaySize  размер дисплея
     * pRealViewSize размер вьюхи превью
     * pAngle        поворот фото
     * pBorderRect   рамка для обрезки документа
     */
    private byte[] mBytes;
    private Point mDisplaySize;
    private Rect mBorderRect;
    private Point mRealViewSize;
    private int mAngle;
    public Bitmap mMiniThumb;
    public String mSavedPhotoUrl;
    public SavedPhotoInfo mSavedPhotoInfo;
    public File mPathToSave;
    private String mFileName;
    private boolean mExactPath;

    public SavePhotoFileTask(Context context) {
        super(context);
    }

    public SavePhotoFileTask(Context context, Object[] params) {
        super(context, params);
        mBytes = (byte[])params[0];
        mDisplaySize = (Point)params[1];
        mRealViewSize = (Point)params[2];
        mAngle = (int)params[3];
        mBorderRect = (Rect)params[4];
        mPathToSave = (File)params[5];
        mExactPath = (boolean)params[6];
    }

    @Override
    public String getTaskId() {
        return null;
    }

    @Override
    public Object execute() {
        if (!mExactPath) {
            File tempPhotoPath = mPathToSave;
            //генерируем имя файла
            int indexPhoto = tempPhotoPath.listFiles().length + 1;
            mFileName = tempPhotoPath.getAbsolutePath() + "/" + indexPhoto + ".png";
        } else {
            mFileName = mPathToSave.getAbsolutePath();
        }
        //обрезаем по фото по рамке
        Bitmap bitmap = BitmapFactory.decodeByteArray(mBytes, 0, mBytes.length);
        mBytes = null;
        L.i("SAVE PHOTO:");
        L.w("bitmap size = " + bitmap.getWidth() + "*" + bitmap.getHeight());
        L.w("Display size = " + mDisplaySize.toString());
        L.w("Border size: left =  " + mBorderRect.left + "; right =  " + mBorderRect.right +
                "; top =  " + mBorderRect.top + "; bottom = " + mBorderRect.bottom);
        L.w(" RealViewSize = " + mRealViewSize.x + "*" + mRealViewSize.y);
        L.w(" mAngle = " + mAngle);
        bitmap = rotate(bitmap);
        L.w("bitmap size = " + bitmap.getWidth() + "*" + bitmap.getHeight());
        bitmap = crop(bitmap);
        bitmap = scaleImage(bitmap, mFileName + ".png");
        saveBitmapToDirectory(bitmap);

        return mSavedPhotoInfo;
    }

    private Bitmap rotate(Bitmap mBitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(mAngle);
        return Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
    }

    /**
     * Обрезка фото по рамке документа.
     *
     * @param pSource
     * @return
     */
    private Bitmap crop(Bitmap pSource) {
        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;
        if (mRealViewSize.x > mRealViewSize.y) {
            float widthRatio = (float) pSource.getWidth() / mRealViewSize.x;
            float heightRatio = (float) pSource.getHeight() / mRealViewSize.y;
            L.w("widthRatio = " + widthRatio);
            L.w("heightRatio = " + heightRatio);

            int ramkaX = (mRealViewSize.x - mDisplaySize.x) / 2;
            int ramkaY = (mRealViewSize.y - mDisplaySize.y) / 2;
            L.w("ramkaX = " + ramkaX);
            L.w("ramkaY = " + ramkaY);

            x = (int) ((mBorderRect.left + ramkaX) * widthRatio);
            y = (int) ((mBorderRect.top + ramkaY) * heightRatio);

            width = ((int) ((mBorderRect.right + ramkaX) * widthRatio) - x);
            height = ((int) ((mBorderRect.bottom + ramkaY) * heightRatio) - y);
        } else {
            float rX = (float) mRealViewSize.x / mDisplaySize.x;
            float rY = (float) mRealViewSize.y / mDisplaySize.y;

            Rect b = new Rect((int) (mBorderRect.left * rX),
                    (int) (mBorderRect.top * rY),
                    (int) (mBorderRect.right * rX),
                    (int) (mBorderRect.bottom * rY));
            L.w("b: left =  " + b.left + "; right =  " + b.right +
                    "; top =  " + b.top + "; bottom = " + b.bottom);
            float widthRatio = (float) pSource.getWidth() / mRealViewSize.x;
            float heightRatio = (float) pSource.getHeight() / mRealViewSize.y;
            x = (int) (b.left * widthRatio);
            y = (int) (b.top * heightRatio);

            width = ((int) (b.right * widthRatio) - x);
            height = ((int) (b.bottom * heightRatio) - y);
        }

        L.w(" CROP: x = " + x + "; y = " + y + "; width = " + width + "; height = " + height);
        return Bitmap.createBitmap(pSource, x, y, width, height);
    }

    private Bitmap scaleImage(Bitmap pBitmap, String pFileName) {
        // FileOutputStream outputStream = null;
        float r = (float) pBitmap.getWidth() / pBitmap.getHeight();
        int w = 1000;
        int h = (int) (1000 / r);
        return Bitmap.createScaledBitmap(pBitmap, w, h, true);
    }

    private void saveBitmapToDirectory(Bitmap imageToSave) {
        File file = new File(mFileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            try {
                imageToSave.compress(Bitmap.CompressFormat.PNG, 50, out);
                out.flush();
            } finally {
                out.close();
            }

            mSavedPhotoInfo = new SavedPhotoInfo();
            mSavedPhotoInfo.mPath = mFileName;
            L.w("Image insert: " + mFileName);
        } catch (FileNotFoundException exception) {
            L.e("File not found", exception);
        } catch (IOException exception) {
            L.e("Failed to insert image", exception);
        }
    }
}


