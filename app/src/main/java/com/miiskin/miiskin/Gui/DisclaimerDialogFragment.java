package com.miiskin.miiskin.Gui;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;

import com.miiskin.miiskin.R;


/**
 * Created by Newshka on 24.06.2015.
 */
public class DisclaimerDialogFragment extends DialogFragment {

    public final static String TAG = "DISCLAIMER_DIALOG_FRAGMENT";

    public interface DisclaimerDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogBackButtonPressed();
    }

    DisclaimerDialogListener mListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DisclaimerDialogListener) activity;

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
        builder.setTitle(R.string.disclaimer);
        builder.setMessage(R.string.disclaimer_dialog_text)
                .setPositiveButton(R.string.start_using_miiskin, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(DisclaimerDialogFragment.this);
                    }
                });
        return builder.create();
    }
}
