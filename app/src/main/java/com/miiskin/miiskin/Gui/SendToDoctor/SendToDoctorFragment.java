package com.miiskin.miiskin.Gui.SendToDoctor;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_to_doctor, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
