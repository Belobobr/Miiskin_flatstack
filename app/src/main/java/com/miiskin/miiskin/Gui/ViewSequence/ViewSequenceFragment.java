package com.miiskin.miiskin.Gui.ViewSequence;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miiskin.miiskin.Data.SequenceData;
import com.miiskin.miiskin.R;

/**
 * Created by Newshka on 26.06.2015.
 */
public class ViewSequenceFragment extends Fragment {
    public static final String EXTRA_SEQUENCE_DATA = "EXTRA_SEQUENCE_DATA";

    SequenceData mSequenceData;

    public static ViewSequenceFragment newInstance(SequenceData sequenceData) {
        ViewSequenceFragment fragment = new ViewSequenceFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(EXTRA_SEQUENCE_DATA, sequenceData);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSequenceData = (SequenceData)getArguments().getSerializable(EXTRA_SEQUENCE_DATA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        final ViewSequenceActivity viewSequenceActivity = (ViewSequenceActivity)getActivity();
        viewSequenceActivity.mActionBarToolbar.setTitle(mSequenceData.mBodyPart.toString());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ViewSequenceActivity viewSequenceActivity = (ViewSequenceActivity)getActivity();
        viewSequenceActivity.mActionBarToolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        viewSequenceActivity.mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
