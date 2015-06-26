package com.miiskin.miiskin.Gui.Camera;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.Preferences;

/**
 * Created by Newshka on 26.06.2015.
 */

public class FteCameraTipsFragment extends DialogFragment {

    public final static String TAG = "FTE_CAMERA_TIP_DIALOG";

    public interface FteCameraTipsListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogBackButtonPressed();
    }

    FteCameraTipsListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FteCameraTipsListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener()
        {
            @Override
            public boolean onKey(android.content.DialogInterface dialog, int keyCode,android.view.KeyEvent event) {

                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK))
                {
                    mListener.onDialogBackButtonPressed();
                }
                return false;

            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DisclaimerAlertDialog);
        builder.setTitle(R.string.tips_for_a_good_photo);
        builder.setMessage(R.string.tips_for_a_good_photo_description)
                .setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(FteCameraTipsFragment.this);
                    }
                });
        return builder.create();
    }

}