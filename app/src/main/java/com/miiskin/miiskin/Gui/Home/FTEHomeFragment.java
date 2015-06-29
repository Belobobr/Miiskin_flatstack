package com.miiskin.miiskin.Gui.Home;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.miiskin.miiskin.Data.Utils;
import com.miiskin.miiskin.R;
import com.miiskin.miiskin.Storage.Preferences;

/**
 * Created by Newshka on 23.06.2015.
 */
public class FTEHomeFragment extends Fragment {

    private RadioButton mFemaleRadioButton;
    private RadioButton mMaleRadioButton;
    private ImageView mMaleImageView;
    private ImageView mFemaleImageView;

    public interface FteCompleteListener {
        public void onFteCompleteDonePressed() ;
    }

    public static FTEHomeFragment newInstance() {
        FTEHomeFragment fragment = new FTEHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFemaleRadioButton.isChecked()) {
            mFemaleImageView.setSelected(true);
        }
        if (mMaleRadioButton.isChecked()) {
            mMaleImageView.setSelected(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fte_home,  container, false);
    }

    private String[] populateYearOfBirth() {
        int from = Preferences.UserInfo.DATE_OF_BIRTH_FROM;
        int to = Preferences.UserInfo.DATE_OF_BIRTH_TO;
        String arrayOfBirth[] = new String[to - from + 1];
        for(int i = 0; i <= to - from; i++) {
            arrayOfBirth[i] = String.valueOf(from + i);
        }
        return arrayOfBirth;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner spinner = (Spinner) view.findViewById(R.id.year_of_birth);

        final ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, populateYearOfBirth());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String dateOfBirth = (String) adapter.getItem(position);
                setDateOfBirth(dateOfBirth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMaleRadioButton = (RadioButton)view.findViewById(R.id.male_button);
        mFemaleRadioButton = (RadioButton)view.findViewById(R.id.female_button);
        mFemaleImageView = (ImageView)view.findViewById(R.id.femaleImageView);
        mMaleImageView = (ImageView)view.findViewById(R.id.maleImageView);

        mMaleRadioButton.setOnClickListener(genderClickListener);
        mFemaleRadioButton.setOnClickListener(genderClickListener);

    }

    private  View.OnClickListener genderClickListener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checked = ((RadioButton) v).isChecked();

            switch(v.getId()) {
                case R.id.male_button:
                    if (checked) {
                        setGender(Preferences.UserInfo.MALE);
                        mMaleImageView.setSelected(true);
                        mFemaleImageView.setSelected(false);
                        mFemaleRadioButton.setChecked(false);
                    }
                    break;
                case R.id.female_button:
                    if (checked) {
                        setGender(Preferences.UserInfo.FEMALE);
                        mMaleImageView.setSelected(false);
                        mFemaleImageView.setSelected(true);
                        mMaleRadioButton.setChecked(false);
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home_fte_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                openListSequence();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setGender(String gender) {
        SharedPreferences settings = getActivity().getSharedPreferences(Preferences.USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Preferences.UserInfo.GENDER, gender);
        editor.commit();
    }

    private void setDateOfBirth(String dateOfBirth) {
        SharedPreferences settings = getActivity().getSharedPreferences(Preferences.USER_INFO, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Preferences.UserInfo.DATE_OF_BIRTH, dateOfBirth);
        editor.commit();
    }

    private void openListSequence() {
        FteCompleteListener fteCompleteListener = (FteCompleteListener)getActivity();
        fteCompleteListener.onFteCompleteDonePressed();
    }
}
