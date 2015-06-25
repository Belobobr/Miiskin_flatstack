package com.miiskin.miiskin.Gui.Home;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miiskin.miiskin.Data.BodyPart;
import com.miiskin.miiskin.Gui.General.PointedImageView;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MolePhotoSequence;

/**
 * Created by Newshka on 25.06.2015.
 */
public class SequenceCursorAdapter extends CursorAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    public SequenceCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    public SequenceCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    public SequenceCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.layout_sequence_adapter, parent, false);
        Holder holder = new Holder();
        holder.mPointedImageView = (PointedImageView)view.findViewById(R.id.bodyPartImageView);
        holder.mBodyPartTextView = (TextView)view.findViewById(R.id.bodyPart);
        holder.mNextPhotoTextView = (TextView)view.findViewById(R.id.nextPhoto);
        holder.mMonitoringStartedTextView = (TextView)view.findViewById(R.id.monitoringStarted);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final String anatomicalSection = cursor.getString(cursor.getColumnIndex(MolePhotoSequence.COLUMN_NAME_ANATOMICAL_SECTION));
        String dateOfCreationSection = cursor.getString(cursor.getColumnIndex(MolePhotoSequence.COLUMN_NAME_DATE_OF_CREATION_SEQUENCE));
        String molePositionX = cursor.getString(cursor.getColumnIndex(MolePhotoSequence.COLUMN_NAME_X_POSITION_OF_MOLE));
        String molePositionY = cursor.getString(cursor.getColumnIndex(MolePhotoSequence.COLUMN_NAME_Y_POSITION_OF_MOLE));
        final Holder holder = (Holder)view.getTag();
        holder.mBodyPartTextView.setText(anatomicalSection);
        holder.mMonitoringStartedTextView.setText(dateOfCreationSection);
        holder.mPointedImageView.post(new Runnable() {
            @Override
            public void run() {
                loadBodyImageView(holder.mPointedImageView, BodyPart.valueOf(anatomicalSection).getDrawableResourceForeground());
            }
        });

    }

    private static class Holder {
        PointedImageView mPointedImageView;
        TextView mBodyPartTextView;
        TextView mNextPhotoTextView;
        TextView mMonitoringStartedTextView;
    }


    private void loadBodyImageView(PointedImageView bodyPartImageView, int resId) {
        final Bitmap bm = decodeSampledBitmapFromResource(mContext.getResources(), resId, bodyPartImageView.getWidth(), bodyPartImageView.getHeight());
        if (bm!=null) {
            bodyPartImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bodyPartImageView.setImageBitmap(bm);
        }
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
