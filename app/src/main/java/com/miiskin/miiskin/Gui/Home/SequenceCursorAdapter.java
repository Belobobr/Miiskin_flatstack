package com.miiskin.miiskin.Gui.Home;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.miiskin.miiskin.Gui.General.PointedImageView;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.MiiskinDatabaseContract.MolePhotoSequence;

/**
 * Created by Newshka on 25.06.2015.
 */
public class SequenceCursorAdapter extends CursorAdapter {

    LayoutInflater mLayoutInflater;

    public SequenceCursorAdapter(Context context, Cursor c) {
        super(context, c);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SequenceCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public SequenceCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        String anatomicalSection = cursor.getString(cursor.getColumnIndex(MolePhotoSequence.COLUMN_NAME_ANATOMICAL_SECTION));
        String dateOfCreationSection = cursor.getString(cursor.getColumnIndex(MolePhotoSequence.COLUMN_NAME_DATE_OF_CREATION_SEQUENCE));
        String molePositionX = cursor.getString(cursor.getColumnIndex(MolePhotoSequence.COLUMN_NAME_X_POSITION_OF_MOLE));
        String molePositionY = cursor.getString(cursor.getColumnIndex(MolePhotoSequence.COLUMN_NAME_Y_POSITION_OF_MOLE));
        Holder holder = (Holder)view.getTag();
        holder.mBodyPartTextView.setText(anatomicalSection);
        holder.mMonitoringStartedTextView.setText(dateOfCreationSection);
    }

    private static class Holder {
        PointedImageView mPointedImageView;
        TextView mBodyPartTextView;
        TextView mNextPhotoTextView;
        TextView mMonitoringStartedTextView;
    }
}
