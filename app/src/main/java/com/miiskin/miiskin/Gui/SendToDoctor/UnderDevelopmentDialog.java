package com.miiskin.miiskin.Gui.SendToDoctor;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 01.07.2015.
 */
public class UnderDevelopmentDialog extends DialogFragment {

    public final static String TAG = "UNDER_DEVELOPMENT_DIALOG";

    public interface UnderDevelopmentDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogBackButtonPressed();
    }

    UnderDevelopmentDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (UnderDevelopmentDialogListener) activity;

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
        builder.setTitle(R.string.under_development);
        builder.setMessage(R.string.under_development_description)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(UnderDevelopmentDialog.this);
                    }
                })
                .setNegativeButton(R.string.send_feedback, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

}
