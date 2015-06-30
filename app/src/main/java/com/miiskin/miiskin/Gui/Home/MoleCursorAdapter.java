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
import com.miiskin.miiskin.Helpers.BitmapDecoder;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.User;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.Mole;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MoleLocation;

/**
 * Created by Newshka on 25.06.2015.
 */
public class MoleCursorAdapter extends CursorAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    public MoleCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    public MoleCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    public MoleCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.layout_sequence_adapter, parent, false);
        Holder holder = new Holder();
        holder.mPointedImageView = (PointedImageView)view.findViewById(R.id.bodyPartImageView);
        holder.mPointedImageView.setPointMode(PointedImageView.PointMode.NORMAL);
        holder.mBodyPartTextView = (TextView)view.findViewById(R.id.bodyPart);
        holder.mNextPhotoTextView = (TextView)view.findViewById(R.id.nextPhoto);
        holder.mMonitoringStartedTextView = (TextView)view.findViewById(R.id.monitoringStarted);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final String anatomicalSection = cursor.getString(cursor.getColumnIndex(MoleLocation.COLUMN_NAME_BODY_PART));
        String dateOfCreationSection = cursor.getString(cursor.getColumnIndex(Mole.COLUMN_NAME_START_OBSERVING_DATE));
        final String molePositionX = cursor.getString(cursor.getColumnIndex(MoleLocation.COLUMN_NAME_X_POSITION_OF_MOLE));
        final String molePositionY = cursor.getString(cursor.getColumnIndex(MoleLocation.COLUMN_NAME_Y_POSITION_OF_MOLE));
        final Holder holder = (Holder)view.getTag();
        holder.mBodyPartTextView.setText(BodyPart.valueOf(anatomicalSection).getResourceIdDescription());
        holder.mMonitoringStartedTextView.setText(dateOfCreationSection);
        holder.mPointedImageView.post(new Runnable() {
            @Override
            public void run() {
                loadBodyImageView(holder.mPointedImageView, BodyPart.valueOf(anatomicalSection).getDrawableResourceForeground());
                holder.mPointedImageView.setPoint(Float.parseFloat(molePositionX),  Float.parseFloat(molePositionY));
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
        final Bitmap bm = BitmapDecoder.decodeSampledBitmapFromResource(mContext.getResources(), resId, bodyPartImageView.getWidth(), bodyPartImageView.getHeight());
        if (bm!=null) {
            bodyPartImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            bodyPartImageView.setImageBitmap(bm);
        }
    }
}
