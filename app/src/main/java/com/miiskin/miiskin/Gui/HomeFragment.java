package com.miiskin.miiskin.Gui;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Created by Newshka on 23.06.2015.
 */
public class HomeFragment extends Fragment {

    Cursor moleSequenceListCursor;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
