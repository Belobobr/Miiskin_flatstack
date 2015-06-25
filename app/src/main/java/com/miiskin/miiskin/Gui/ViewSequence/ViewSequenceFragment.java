package com.miiskin.miiskin.Gui.ViewSequence;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 26.06.2015.
 */
public class ViewSequenceFragment extends Fragment {

    public static ViewSequenceFragment newInstance() {
        ViewSequenceFragment fragment = new ViewSequenceFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ViewSequenceActivity createSequenceActivity = (ViewSequenceActivity)getActivity();
        createSequenceActivity.mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        createSequenceActivity.mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_sequence, container, false);
    }
}
