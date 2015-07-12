package com.miiskin.miiskin.Gui.SendToDoctor;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.miiskin.miiskin.Data.AnalyticsNames;
import com.miiskin.miiskin.Data.L;
import com.miiskin.miiskin.MiiskinApplication;
import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 27.06.2015.
 */
public class SendToDoctorFragment extends Fragment{

    public static SendToDoctorFragment newInstance() {
        SendToDoctorFragment fragment = new SendToDoctorFragment();
        Bundle arguments = new Bundle();
        fragment.setArguments(arguments);
        return fragment;
    }

    private CheckBox mLicenseAgreementCheckBox;
    private Tracker mTracker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_to_doctor, container, false);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MiiskinApplication application = (MiiskinApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();
        L.i("Setting screen name: " + AnalyticsNames.SEND_TO_DOCTOR);
        mTracker.setScreenName(AnalyticsNames.SEND_TO_DOCTOR);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View aboutOurDoctor = view.findViewById(R.id.about_our_doctor);
        final View aboutOurDoctorDescription = view.findViewById(R.id.about_our_doctor_description);
        aboutOurDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aboutOurDoctorDescription.getVisibility() == View.VISIBLE) {
                    aboutOurDoctorDescription.setVisibility(View.GONE);
                } else {
                    aboutOurDoctorDescription.setVisibility(View.VISIBLE);
                }
                L.i("Event occur: " + "Category: " + AnalyticsNames.EventCategory.INFORMATION + ";Action : " + AnalyticsNames.EventAction.ABOUT_DOCTOR_ACTION);
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory(AnalyticsNames.EventCategory.INFORMATION)
                        .setAction(AnalyticsNames.EventAction.ABOUT_DOCTOR_ACTION)
                        .setValue(1)
                        .build());
            }
        });
        Button sendToDoctorButton = (Button)view.findViewById(R.id.send_to_doctor_button);
        sendToDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmail() && checkAgreement()) {
                    DialogFragment dialog = new UnderDevelopmentDialog();
                    dialog.show(getActivity().getFragmentManager(), UnderDevelopmentDialog.TAG);
                }
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory(AnalyticsNames.EventCategory.ASSESSMENT)
                        .setAction(AnalyticsNames.EventAction.PAY_AND_SEND_PHOTO)
                        .setValue(1)
                        .build());
            }
        });
        mLicenseAgreementCheckBox = (CheckBox)view.findViewById(R.id.licence_agreement_check_box);
    }

    private boolean checkEmail() {
        return true;
    }

    private boolean checkAgreement() {
        return  mLicenseAgreementCheckBox.isChecked();
    }

}
